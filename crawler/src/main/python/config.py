import os
from dotenv import load_dotenv

load_dotenv()

# --- LLM and API Configuration ---
OPENAI_API_BASE = os.getenv("OPENAI_API_BASE")
OPENAI_API_KEY = os.getenv("OPENAI_API_KEY")
LLM_MODEL_NAME = os.getenv("LLM_MODEL_NAME")
USER_AGENT = os.getenv("USER_AGENT")

# --- Playwright Configuration ---
PLAYWRIGHT_PAGE_LOAD_TIMEOUT = 30000
PLAYWRIGHT_JS_WAIT_TIMEOUT = 5000

# --- Crawl Parameters ---
MAX_PAGES_DEFAULT = 100
# NUM_SEARCH_RESULTS_PER_REGION = 100
NUM_SEARCH_RESULTS_PER_REGION = 5
CRAWLS_PER_SITE = 5
BATCH_SIZE = 2
MAX_CHARS_PER_BATCH = 240000 # 60k tokens * 4 chars/token

# --- Search and Link Finding ---
SAGRA_KEYWORDS = ["sagra", "sagre", "evento", "eventi", "programma", "dettagli", "edizione", "festa", "feste"]

# --- Geography ---
REGIONS_OF_ITALY = [
    "Abruzzo", "Basilicata", "Calabria", "Campania", "Emilia-Romagna",
    "Friuli-Venezia Giulia", "Lazio", "Liguria", "Lombardia", "Marche",
    "Molise", "Piemonte", "Puglia", "Sardegna", "Sicilia", "Toscana",
    "Trentino-Alto Adige", "Umbria", "Valle d'Aosta", "Veneto"
]

# --- Prompts ---
def get_batch_extraction_system_prompt(region: str, year: str):
    return f"""You are a data extraction expert tasked with extracting information about Italian food festivals ('sagre', 'feste', 'eventi').
Your goal is to use the provided tool to record the details of any event you find in the concatenated text which contains multiple web pages.

- Strictly focus on events happening within the region of {region}.
- The current year is {year}; ignore all events from past years.
- If multiple distinct events are mentioned, record each one.
- **Crucially, you must populate the `source_url` field for each event with the URL provided in the text block's header.**
- If a piece of information for a field is not available, use 'N/A'.
- If no relevant events are found in the text, simply return an empty list.
- Return results in Italian.
"""

def get_batch_extraction_user_prompt(text_content: str):
    return f"""Please extract the event details from the following text, which contains content from multiple web pages:

{text_content}"""

