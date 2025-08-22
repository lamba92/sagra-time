import os
import re
import json
import asyncio
import datetime
import validators
from tqdm import tqdm
from dotenv import load_dotenv
from bs4 import BeautifulSoup
from langchain_openai import ChatOpenAI
from urllib.parse import urljoin, urlparse
from langgraph.graph import StateGraph, END
from typing import List, Set, TypedDict, Optional, Dict
from langchain_community.utilities import SearxSearchWrapper
from langchain_core.messages import HumanMessage, SystemMessage
from playwright.async_api import async_playwright, TimeoutError as PlaywrightTimeoutError

load_dotenv()

# --- Agent State ---
class AgentState(TypedDict):
    initial_urls: List[str]
    urls_to_visit: List[str]
    visited_urls: Set[str]
    current_url: Optional[str]
    current_page_content: Optional[str]
    error_message: Optional[str]
    found_sagre: List[Dict[str, str]]
    is_relevant: bool
    max_total_pages_to_crawl: int
    total_pages_crawled: int
    max_pages_per_domain: int
    pages_crawled_per_domain: Dict[str, int]
    initial_domains_set: Set[str]
    region: str
    year: str

# --- LLM Configuration ---
llm = ChatOpenAI(
    openai_api_base=os.getenv("OPENAI_API_BASE"),
    openai_api_key=os.getenv("OPENAI_API_KEY"),
    model_name=os.getenv("LLM_MODEL_NAME"),
    temperature=.7,
)

# --- Crawl Configuration ---
USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36"
REQUEST_TIMEOUT = 15
MAX_PAGES_DEFAULT = 100
PLAYWRIGHT_PAGE_LOAD_TIMEOUT = 30000
PLAYWRIGHT_JS_WAIT_TIMEOUT = 10000

# --- Helper Functions ---
def get_domain(url: str) -> Optional[str]:
    try:
        return urlparse(url).netloc
    except Exception:
        return None

# --- Agent Nodes ---
def initialize_crawl(state: AgentState) -> AgentState:
    print("--- INITIALIZING CRAWL ---")
    initial_urls = state["initial_urls"]
    initial_domains_set = {get_domain(url) for url in initial_urls if get_domain(url)}
    return {
        **state,
        "urls_to_visit": list(initial_urls),
        "visited_urls": set(),
        "found_sagre": [],
        "total_pages_crawled": 0,
        "pages_crawled_per_domain": {domain: 0 for domain in initial_domains_set},
        "initial_domains_set": initial_domains_set,
    }

def select_next_url(state: AgentState) -> AgentState:
    urls_to_visit = state["urls_to_visit"]
    pages_crawled_per_domain = state["pages_crawled_per_domain"]
    max_pages_per_domain = state["max_pages_per_domain"]
    
    next_url_to_process = None
    url_index_in_queue = -1

    for i, url in enumerate(urls_to_visit):
        domain = get_domain(url)
        if domain and domain not in pages_crawled_per_domain:
            pages_crawled_per_domain[domain] = 0
        
        if domain and pages_crawled_per_domain.get(domain, 0) < max_pages_per_domain:
            next_url_to_process = url
            url_index_in_queue = i
            break

    if next_url_to_process:
        updated_urls_to_visit = list(urls_to_visit)
        updated_urls_to_visit.pop(url_index_in_queue)
        return {
            **state,
            "current_url": next_url_to_process,
            "urls_to_visit": updated_urls_to_visit,
            "current_page_content": None,
            "error_message": None,
            "is_relevant": False,
        }
    else:
        print("  No more valid URLs to process.")
        return {**state, "current_url": None}

async def fetch_with_playwright(url: str) -> Optional[str]:
    try:
        async with async_playwright() as p:
            browser = await p.firefox.launch(headless=True)
            context = await browser.new_context(user_agent=USER_AGENT, java_script_enabled=True)
            page = await context.new_page()
            await page.goto(url, timeout=PLAYWRIGHT_PAGE_LOAD_TIMEOUT, wait_until="domcontentloaded")
            await page.wait_for_timeout(PLAYWRIGHT_JS_WAIT_TIMEOUT)
            content = await page.content()
            await browser.close()
            return content
    except Exception as e:
        print(f"    Playwright error for {url}: {str(e)[:200]}...")
        return None

async def fetch_page(state: AgentState) -> AgentState:
    current_url = state["current_url"]
    if not current_url:
        return {**state, "error_message": "No URL to fetch." }

    new_total_pages_crawled = state["total_pages_crawled"] + 1
    domain = get_domain(current_url)
    new_pages_crawled_per_domain = state["pages_crawled_per_domain"].copy()
    if domain:
        new_pages_crawled_per_domain[domain] = new_pages_crawled_per_domain.get(domain, 0) + 1

    print(f"  Fetching page with Playwright: {current_url}")
    page_content = await fetch_with_playwright(current_url)

    if not page_content:
        return {
            **state,
            "current_page_content": None,
            "visited_urls": state["visited_urls"].union({current_url}),
            "total_pages_crawled": new_total_pages_crawled,
            "pages_crawled_per_domain": new_pages_crawled_per_domain,
            "error_message": "Failed to fetch content with Playwright.",
        }

    return {
        **state,
        "current_url": current_url,
        "current_page_content": page_content,
        "visited_urls": state["visited_urls"].union({current_url}),
        "total_pages_crawled": new_total_pages_crawled,
        "pages_crawled_per_domain": new_pages_crawled_per_domain,
        "error_message": None,
    }

def check_relevance(state: AgentState) -> AgentState:
    page_content = state["current_page_content"]
    if not page_content:
        return {**state, "is_relevant": False}

    soup = BeautifulSoup(page_content, 'html.parser')
    text_content = soup.get_text(separator=" ", strip=True)[:15000] # Limit context

    system_prompt = "You are an expert at identifying Italian food festivals ('sagre'). Your task is to determine if the provided text contains information about a sagra. Respond with only 'YES' or 'NO'."
    user_prompt = f"Based on the following text, is this page about a sagra?\n\nTEXT: {text_content}"
    
    try:
        messages = [SystemMessage(content=system_prompt), HumanMessage(content=user_prompt)]
        response = llm.invoke(messages)
        answer = response.content.strip().upper()
        is_relevant = "YES" in answer
        print(f"  Relevance check for {state['current_url']}: {'Relevant' if is_relevant else 'Not Relevant'}")
        return {**state, "is_relevant": is_relevant}
    except Exception as e:
        print(f"      Error during relevance check: {e}")
        return {**state, "is_relevant": False}

def extract_sagra_data(state: AgentState) -> AgentState:
    page_content = state["current_page_content"]
    current_url = state["current_url"]
    if not page_content or not current_url or not state["is_relevant"]:
        return state

    print(f"  Extracting data from relevant page: {current_url}")
    soup = BeautifulSoup(page_content, 'html.parser')
    text_content = soup.get_text(separator=" ", strip=True)[:8000]
    
    system_prompt = f"""You are a data extraction expert.
     Extract information about the 'sagra' from the text.
     Format the output as a list with a JSON object with the following keys: 'location', 'start_date', 'stop_date', 'name', 'food_list', 'info_summary', 'official_url'.
     The 'food_list' must be a list whose items are strings, not a string with items listed inside it. 
     If a piece of information is not available, use 'N/A'. If more than one sagra is present, return a list with one object for each sagra.
     Strictly focus on 'sagre' happening inside the region of {state['region']}, ignoring them otherwise.
     The current year is {state['year']}, ignore all past sagre."""
    user_prompt = f"Extract the sagra details from this text:\n\n{text_content}"
    
    try:
        messages = [SystemMessage(content=system_prompt), HumanMessage(content=user_prompt)]
        response = llm.invoke(messages)
        response_text = response.content.strip()
        print(f"{response_text}")

        # Use regex to find the JSON list, allowing for markdown code blocks
        match = re.search(r'\[.*\]', response_text, re.DOTALL)
        if not match:
            print("      Could not find a JSON list in the response.")
            return state
        
        json_text = match.group(0)
        extracted_sagre = json.loads(json_text)
        
        if not isinstance(extracted_sagre, list):
            print(f"      Extracted data is not a list, but {type(extracted_sagre)}")
            return state

        for sagra in extracted_sagre:
            sagra['source_url'] = current_url
        
        sagra_names = [s.get('name', 'N/A') for s in extracted_sagre]
        print(f"    Extracted: {sagra_names}")
        
        return {**state, "found_sagre": state["found_sagre"] + extracted_sagre}
    except (json.JSONDecodeError, Exception) as e:
        print(f"      Error during data extraction: {e}")
        return state # Return original state on error

def find_new_links(state: AgentState) -> AgentState:
    page_content = state["current_page_content"]
    current_url = state["current_url"]
    if not page_content or not current_url:
        return state

    print(f"  Finding new links on page: {current_url}")
    soup = BeautifulSoup(page_content, 'html.parser')
    new_links = set()
    sagra_keywords = ["sagra", "sagre", "evento", "eventi", "programma", "dettagli", "edizione", "festa", "feste"]

    for link_tag in soup.find_all('a', href=True):
        href = link_tag['href']
        abs_url = urljoin(current_url, href)
        parsed_url = urlparse(abs_url)
        cleaned_url = parsed_url._replace(query="", fragment="").geturl()

        if not validators.url(cleaned_url) or parsed_url.scheme not in ['http', 'https']:
            continue
        if cleaned_url in state["visited_urls"] or cleaned_url in state["urls_to_visit"]:
            continue
        
        link_text = link_tag.get_text(strip=True).lower()
        
        if any(keyword in cleaned_url.lower() for keyword in sagra_keywords) or \
           any(keyword in link_text for keyword in sagra_keywords):
            new_links.add(cleaned_url)
        ## TODO for now we bypass this part because it's painfully slow, might uncomment when using a faster model
        # else:
        #     try:
        #         print(f"    Asking LLM about link: {cleaned_url}")
        #         system_prompt = f"You are an expert at identifying Italian food festivals ('sagre'). Your task is to determine if the provided link is about a sagra in the region of {state['region']} for the year {state['year']}. Respond with only 'YES' or 'NO'."
        #         user_prompt = f"Based on the following URL and link text, could this page be about 'sagre', 'feste' or 'events' in {state['region']}? URL: {cleaned_url} Link Text: {link_text}"
        #
        #         messages = [SystemMessage(content=system_prompt), HumanMessage(content=user_prompt)]
        #         response = llm.invoke(messages)
        #         answer = response.content.strip().upper()
        #
        #         if "YES" in answer:
        #             print(f"      LLM says YES.")
        #             new_links.add(cleaned_url)
        #         else:
        #             print(f"      LLM says NO.")
        #     except Exception as e:
        #         print(f"      Error during LLM link check: {e}")


    print(f"    Found {len(new_links)} new relevant links.")
    return {**state, "urls_to_visit": state["urls_to_visit"] + list(new_links)}


# --- Graph Definition ---
workflow = StateGraph(AgentState)
workflow.add_node("initialize_crawl", initialize_crawl)
workflow.add_node("select_next_url", select_next_url)
workflow.add_node("fetch_page", fetch_page)
workflow.add_node("check_relevance", check_relevance)
workflow.add_node("extract_sagra_data", extract_sagra_data)
workflow.add_node("find_new_links", find_new_links)

workflow.set_entry_point("initialize_crawl")
workflow.add_edge("initialize_crawl", "select_next_url")

workflow.add_conditional_edges(
    "select_next_url",
    lambda state: "fetch_page" if state.get("current_url") and state.get("total_pages_crawled", 0) < state.get("max_total_pages_to_crawl", MAX_PAGES_DEFAULT) else END,
    {"fetch_page": "fetch_page", END: END}
)

workflow.add_conditional_edges(
    "fetch_page",
    lambda state: "check_relevance" if state.get("current_page_content") else "select_next_url",
    {"check_relevance": "check_relevance", "select_next_url": "select_next_url"}
)

workflow.add_conditional_edges(
    "check_relevance",
    lambda state: "extract_sagra_data" if state.get("is_relevant") else "find_new_links",
    {
        "extract_sagra_data": "extract_sagra_data",
        "find_new_links": "find_new_links"
    }
)

workflow.add_edge("extract_sagra_data", "find_new_links")
workflow.add_edge("find_new_links", "select_next_url")

app = workflow.compile()

# --- Main Execution ---
def get_initial_urls_from_searxng(query: str, num_results: int = 10) -> List[str]:
    print(f"--- QUERYING SEARXNG FOR: '{query}' ---")
    try:
        search = SearxSearchWrapper(searx_host=os.getenv("SEARXNG_BASE_URL"))
        results = search.results(query, num_results=num_results)
        return [result['link'] for result in results]
    except Exception as e:
        print(f"  Error querying SearXNG: {e}")
        return []

async def run_crawl_for_all_regions():
    regions_of_italy = [
        "Abruzzo", "Basilicata", "Calabria", "Campania", "Emilia-Romagna",
        "Friuli-Venezia Giulia", "Lazio", "Liguria", "Lombardia", "Marche",
        "Molise", "Piemonte", "Puglia", "Sardegna", "Sicilia", "Toscana",
        "Trentino-Alto Adige", "Umbria", "Valle d'Aosta", "Veneto"
    ]
    num_search_results_per_region = 50
    crawls_per_site = 5
    current_year = datetime.datetime.now().year
    for region in regions_of_italy:
        search_query = f"sagre in {region} {current_year}"
        print(f"\n--- STARTING REGION: {region} ---")

        initial_urls = get_initial_urls_from_searxng(search_query, num_search_results_per_region)
        if not initial_urls:
            print(f"  No initial URLs found for {region}. Skipping.")
            continue
        
        print(f"  Found {len(initial_urls)} initial URLs.")
        max_total_pages_this_run = len(initial_urls) * crawls_per_site
        
        initial_state = {
            "initial_urls": initial_urls,
            "max_total_pages_to_crawl": max_total_pages_this_run,
            "max_pages_per_domain": crawls_per_site,
            "region": region,
            "year": current_year
        }

        final_state = None
        try:
            async for event in app.astream(initial_state, config={"recursion_limit": max_total_pages_this_run * 3}):
                node_name = list(event.keys())[0]
                final_state = event[node_name] # Keep track of the latest state
                
                if final_state and not final_state.get("current_url") and not final_state.get("urls_to_visit"):
                    print(f"No more URLs to visit for {region}. Stopping.")
                    break

        except Exception as e:
            print(f"\n--- CRAWL FOR REGION {region} FAILED ---")
            print(f"Error: {e}")
            import traceback
            traceback.print_exc()

        print(f"\n--- CRAWL FINISHED FOR REGION: {region} ---")
        if final_state:
            found_sagre = final_state.get('found_sagre', [])
            print(f"Total sagre found: {len(found_sagre)}")

            if found_sagre:
                timestamp = datetime.datetime.now().strftime("%Y%m%d_%H%M%S")
                filename = f"sagre_{region.lower()}_{timestamp}.json"
                with open(filename, "w", encoding="utf-8") as f:
                    json.dump(found_sagre, f, ensure_ascii=False, indent=4)
                print(f"Saved sagre data to {filename}")
            else:
                print("No sagre data found for this region.")
        else:
            print(f"Crawl for {region} did not produce a final state.")

    print("\n\n--- ALL REGIONS PROCESSED ---")

if __name__ == "__main__":
    asyncio.run(run_crawl_for_all_regions())