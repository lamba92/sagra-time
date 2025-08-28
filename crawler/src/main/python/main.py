import os
import sys
# Ensure the current directory is in the path for module imports.
sys.path.append(os.path.dirname(os.path.abspath(__file__)))
import json
import asyncio
import datetime
from typing import Any
from ddgs import DDGS
from playwright.async_api import async_playwright

# Local Imports
import config
from state import AgentState
from graph import create_workflow_app


# --- Crawler Implementation ---
def get_initial_urls_from_ddgs(query: str, num_results: int = 10) -> list[dict[str, Any]] | None:
    print(f"--- QUERYING DDGS FOR: '{query}' ---")
    try:
        results = DDGS(timeout=30).text(query, region="it-it", max_results=num_results, timelimit='y')
        return results
    except Exception as e:
        print(f"  Error querying DDGS: {e}")
        return None


async def run_crawler():
    current_year = datetime.datetime.now().year

    async with async_playwright() as p:
        browser = await p.firefox.launch(headless=True)

        # Using slice for testing
        for region in config.REGIONS_OF_ITALY[:1]:
            search_query = f"sagre in {region} {current_year}"
            print(f"\n--- STARTING REGION: {region} ---")

            context = await browser.new_context(user_agent=config.USER_AGENT, java_script_enabled=True)
            
            # Local workflow app instance with the current browser context
            app = create_workflow_app(context)

            initial_urls_results = get_initial_urls_from_ddgs(search_query, config.NUM_SEARCH_RESULTS_PER_REGION)
            if not initial_urls_results:
                print(f"  No initial URLs found for {region}. Skipping.")
                await context.close()
                continue
            
            initial_urls = [result['href'] for result in initial_urls_results]
            print(f"  Found {len(initial_urls)} initial URLs.")
            max_total_pages_this_run = len(initial_urls) * config.CRAWLS_PER_SITE

            initial_state = AgentState(
                initial_urls=initial_urls,
                max_total_pages_to_crawl=max_total_pages_this_run,
                max_pages_per_domain=config.CRAWLS_PER_SITE,
                region=region,
                year=str(current_year)
            )

            final_state = None
            try:
                # The recursion limit is set based on the number of pages and the number of steps in the graph loop.
                recursion_limit = (max_total_pages_this_run // config.BATCH_SIZE + 1) * 4 
                async for event in app.astream(initial_state, config={"recursion_limit": recursion_limit}):
                    node_name = list(event.keys())[0]
                    final_state = event[node_name]
                    
                    # Check if the crawl should stop
                    if not final_state.get("url_batch") and not final_state.get("urls_to_visit"):
                        print(f"No more URLs to visit for {region}. Stopping.")
                        break

            except Exception as e:
                print(f"\n--- CRAWL FOR REGION {region} FAILED ---")
                print(f"Error: {e}")
                import traceback
                traceback.print_exc()
            
            await context.close()

        await browser.close()

        print(f"\n--- CRAWL FINISHED FOR REGION: {region} ---")
        if final_state:
            found_sagre = final_state.get('found_sagre', [])
            print(f"Total sagre found: {len(found_sagre)}")

            if found_sagre:
                timestamp = datetime.datetime.now().strftime("%Y%m%d_%H%M%S")
                filename = f"crawling_output/sagre_{region.lower()}_{timestamp}.json"
                with open(filename, "w", encoding="utf-8") as f:
                    json.dump(found_sagre, f, ensure_ascii=False, indent=4)
                print(f"Saved sagre data to {filename}")
            else:
                print("No sagre data found for this region.")
        else:
            print(f"Crawl for {region} did not produce a final state.")

    print("\n\n--- ALL REGIONS PROCESSED ---")

if __name__ == "__main__":
    asyncio.run(run_crawler())