import hashlib
import asyncio
from functools import partial
from typing import Optional
from urllib.parse import urljoin, urlparse

import validators
from bs4 import BeautifulSoup
from langchain_core.messages import HumanMessage, SystemMessage
from langchain_openai import ChatOpenAI
from langgraph.graph import END, StateGraph
from playwright.async_api import BrowserContext

import config
from state import AgentState
from data_models import SagraList

# --- LLM Configuration ---
llm = ChatOpenAI(
    openai_api_base=config.OPENAI_API_BASE,
    openai_api_key=config.OPENAI_API_KEY,
    model_name=config.LLM_MODEL_NAME,
    temperature=.7,
).with_structured_output(SagraList)

# --- Helper Functions ---
def get_domain(url: str) -> Optional[str]:
    try:
        return urlparse(url).netloc
    except Exception:
        return None

# --- Agent Nodes ---
def initialize_crawl(state: AgentState) -> AgentState:
    """Initialize the crawler."""
    print("--- INITIALIZING CRAWL ---")
    initial_domains_set = {get_domain(url) for url in state.initial_urls if get_domain(url)}
    state.urls_to_visit = list(state.initial_urls)
    state.visited_urls = set()
    state.visited_content_hashes = set()
    state.found_sagre = []
    state.total_pages_crawled = 0
    state.pages_crawled_per_domain = {domain: 0 for domain in initial_domains_set}
    state.initial_domains_set = initial_domains_set
    return state

def select_url_batch(state: AgentState) -> AgentState:
    """Selects a batch of unique URLs to process, respecting domain limits."""
    batch_set = set() # Use a set to automatically handle duplicates
    
    # Keep trying to fill the batch as long as there are URLs to visit
    # and the batch isn't full yet.
    while len(batch_set) < config.BATCH_SIZE and state.urls_to_visit:
        url = state.urls_to_visit.pop(0) # Take from the beginning for breadth-first search

        # Skip already visited URLs
        if url in state.visited_urls:
            continue

        # Check domain-specific counter
        domain = get_domain(url)
        if domain:
            if domain not in state.pages_crawled_per_domain:
                state.pages_crawled_per_domain[domain] = 0
            
            if state.pages_crawled_per_domain.get(domain, 0) >= state.max_pages_per_domain:
                continue # Skip this URL, domain limit reached
        
        # If all checks pass, add to the batch
        batch_set.add(url)

    state.url_batch = list(batch_set) # Convert set to list for processing
    state.content_batch = []
    state.error_message = None
    
    if state.url_batch:
        print(f"--- SELECTED BATCH OF {len(state.url_batch)} URLS ---")
    else:
        print("--- NO MORE VALID URLS TO PROCESS ---")

    return state

async def fetch_page_batch(state: AgentState, context: BrowserContext) -> AgentState:
    """Fetches content for a batch of URLs asynchronously."""
    if not state.url_batch:
        state.error_message = "No URL batch to fetch."
        return state

    async def fetch_one(url: str):
        domain = get_domain(url)
        if domain:
            state.pages_crawled_per_domain[domain] += 1

        print(f"  Fetching page: {url}")
        page_content = None
        page = await context.new_page()
        try:
            await page.goto(url, timeout=config.PLAYWRIGHT_PAGE_LOAD_TIMEOUT, wait_until="domcontentloaded")
            await page.wait_for_timeout(config.PLAYWRIGHT_JS_WAIT_TIMEOUT)
            page_content = await page.content()
        except Exception as e:
            print(f"    Playwright error for {url}: {str(e)[:200]}...")
        finally:
            await page.close()
        
        state.visited_urls.add(url)
        state.total_pages_crawled += 1
        return {"url": url, "content": page_content}

    results = await asyncio.gather(*(fetch_one(url) for url in state.url_batch))
    
    # Filter out failed fetches and duplicate content
    for res in results:
        if not res["content"]:
            continue
        
        soup = BeautifulSoup(res["content"], 'html.parser')
        body_text = soup.body.get_text(separator=" ", strip=True) if soup.body else ""
        content_hash = hashlib.sha256(body_text.encode('utf-8')).hexdigest()

        if content_hash in state.visited_content_hashes:
            print(f"  Skipping already processed content for: {res['url']}")
            continue
            
        state.visited_content_hashes.add(content_hash)
        state.content_batch.append(res)

    print(f"  Successfully fetched and de-duplicated {len(state.content_batch)} pages.")
    return state

def process_batch(state: AgentState) -> AgentState:
    """Processes a batch of pages, extracts sagre, and finds new links."""
    if not state.content_batch:
        print("  No content in batch to process.")
        return state

    # 1. Construct the combined text for the LLM
    combined_text = ""
    for page_data in state.content_batch:
        soup = BeautifulSoup(page_data["content"], 'html.parser')
        text_content = soup.get_text(separator=" ", strip=True)
        
        # Truncate individual page content to avoid massive combined text
        max_len = config.MAX_CHARS_PER_BATCH // len(state.content_batch) if state.content_batch else 0
        truncated_text = text_content[:max_len]

        combined_text += f"--- START OF CONTENT FROM: {page_data['url']} ---\n"
        combined_text += truncated_text
        combined_text += f"\n--- END OF CONTENT FROM: {page_data['url']} ---\n\n"

    # 2. Extract Sagra Data from the combined text
    print(f"--- EXTRACTING DATA FROM BATCH ({len(state.content_batch)} pages) ---")
    system_prompt = config.get_batch_extraction_system_prompt(state.region, state.year)
    user_prompt = config.get_batch_extraction_user_prompt(combined_text)
    print(f"  Combined text length: {len(combined_text) // 4} tokens")
    
    try:
        messages = [SystemMessage(content=system_prompt), HumanMessage(content=user_prompt)]
        response = llm.invoke(messages)
        
        extracted_sagre_list = response.sagre
        if not extracted_sagre_list:
            print("      LLM did not extract any sagre from this batch.")
        else:
            sagra_names = [s.name for s in extracted_sagre_list]
            print(f"    Extracted: {sagra_names}")
            for sagra in extracted_sagre_list:
                state.found_sagre.append(sagra.model_dump())
        
    except Exception as e:
        print(f"      Error during batch data extraction: {e}")

    # 3. Find New Links from all pages in the batch
    print(f"--- FINDING NEW LINKS IN BATCH ---")
    new_links = set()
    for page_data in state.content_batch:
        current_url = page_data["url"]
        soup = BeautifulSoup(page_data["content"], 'html.parser')

        for link_tag in soup.find_all('a', href=True):
            href = link_tag['href']
            abs_url = urljoin(current_url, href)
            parsed_url = urlparse(abs_url)
            cleaned_url = parsed_url._replace(query="", fragment="").geturl()

            if not validators.url(cleaned_url) or parsed_url.scheme not in ['http', 'https']:
                continue
            if cleaned_url in state.visited_urls or cleaned_url in state.urls_to_visit:
                continue
            
            link_text = link_tag.get_text(strip=True).lower()
            
            if any(keyword in cleaned_url.lower() for keyword in config.SAGRA_KEYWORDS) or \
               any(keyword in link_text for keyword in config.SAGRA_KEYWORDS):
                new_links.add(cleaned_url)

    print(f"    Found {len(new_links)} new relevant links from batch.")
    state.urls_to_visit.extend(list(new_links))
    
    return state

# --- Graph Factory ---
def create_workflow_app(context: BrowserContext):
    workflow = StateGraph(AgentState)

    workflow.add_node("initialize_crawl", initialize_crawl)
    workflow.add_node("select_url_batch", select_url_batch)
    
    fetch_page_batch_with_context = partial(fetch_page_batch, context=context)
    workflow.add_node("fetch_page_batch", fetch_page_batch_with_context)
    
    workflow.add_node("process_batch", process_batch)

    workflow.set_entry_point("initialize_crawl")
    workflow.add_edge("initialize_crawl", "select_url_batch")
    
    workflow.add_conditional_edges(
        "select_url_batch",
        lambda state: "fetch_page_batch" if state.url_batch and state.total_pages_crawled < state.max_total_pages_to_crawl else END,
        {"fetch_page_batch": "fetch_page_batch", END: END}
    )
    
    workflow.add_edge("fetch_page_batch", "process_batch")
    workflow.add_edge("process_batch", "select_url_batch")
    
    return workflow.compile()