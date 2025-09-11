from typing import List, Set, Optional, Dict, Any
from pydantic import BaseModel

# --- Agent State ---
class AgentState(BaseModel):
    initial_urls: List[str] = []
    urls_to_visit: List[str] = []
    visited_urls: Set[str] = set()
    visited_content_hashes: Set[str] = set()

    # Batch processing fields
    url_batch: List[str] = []
    content_batch: List[Dict[str, str]] = []

    error_message: Optional[str] = None
    found_sagre: List[Dict[str, Any]] = []
    
    max_total_pages_to_crawl: int = 0
    total_pages_crawled: int = 0
    max_pages_per_domain: int = 0
    pages_crawled_per_domain: Dict[str, int] = {}
    initial_domains_set: Set[str] = set()
    region: str = ""
    year: str = ""
