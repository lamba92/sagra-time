import json
import re
import argparse
import os
from typing import Dict, Any, List, Optional, Tuple

# Mapping of Italian months to month numbers
MONTH_MAP = {
    'gennaio': '01', 'febbraio': '02', 'marzo': '03', 'aprile': '04',
    'maggio': '05', 'giugno': '06', 'luglio': '07', 'agosto': '08',
    'settembre': '09', 'ottobre': '10', 'novembre': '11', 'dicembre': '12'
}

def normalize_date(date_str: str) -> Optional[Tuple[str, str]]:
    """Converts various Italian date formats to a tuple of (YYYY-MM-DD, YYYY-MM-DD)."""
    if not date_str or date_str.lower() == 'n/a':
        return None

    date_str = date_str.lower().strip().replace('\u202f', ' ')

    # Format: YYYY-MM-DD (already correct)
    if re.match(r'^\d{4}-\d{2}-\d{2}$', date_str):
        return date_str, date_str

    # Format: DD-MM-YYYY or DD/MM/YYYY
    match = re.match(r'(\d{1,2})[-/](\d{1,2})[-/](\d{4})', date_str)
    if match:
        day, month, year = match.groups()
        date = f"{year}-{month.zfill(2)}-{day.zfill(2)}"
        return date, date

    # Formats with month name
    for month_name, month_num in MONTH_MAP.items():
        if month_name in date_str:
            # Format: DD-DD [mese] YYYY (e.g., "28-29 agosto 2025")
            range_match = re.search(r'(\d{1,2})-(\d{1,2})\s+' + month_name + r'\s+(\d{4})', date_str)
            if range_match:
                day_start, day_end, year = range_match.groups()
                start_date = f"{year}-{month_num}-{day_start.zfill(2)}"
                end_date = f"{year}-{month_num}-{day_end.zfill(2)}"
                return start_date, end_date

            # Format: DD [mese] YYYY (e.g., "6 agosto 2025")
            single_match = re.search(r'(\d{1,2})\s+' + month_name + r'\s+(\d{4})', date_str)
            if single_match:
                day, year = single_match.groups()
                date = f"{year}-{month_num}-{day.zfill(2)}"
                return date, date
    
    print(f"[Warning] Could not parse date: {date_str}")
    return None


def clean_name(name: str, primary_location: Optional[str] = None) -> str:
    """Cleans the event name by removing edition numbers, location suffixes, and other noise."""
    if not name or name.lower() == 'n/a':
        return ""

    # A list of regex patterns to remove common noise from event names.
    noise_patterns = [
        # Matches edition numbers like "38ª edizione", "47esima", or "VII Edizione"
        r'\s*\b(\d{1,2}(ª|°|a|o|esima)?|([IVXLCDM]+))\s*(edizione)?\b',
        # Matches the literal string "(sagra)"
        r'\s*\(sagra\)\s*'
    ]

    for pattern in noise_patterns:
        name = re.sub(pattern, '', name, flags=re.IGNORECASE)

    # Remove the primary location if it's used as a suffix, like "Event Name - Location".
    if primary_location:
        location_suffix_pattern = r'\s*[-–]\s*' + re.escape(primary_location) + '$'
        name = re.sub(location_suffix_pattern, '', name, flags=re.IGNORECASE)

    name = re.sub(r'\s*-\s*$', '', name)

    return name.strip().title()

def get_primary_location(location: str) -> str:
    """Extracts the primary city/town from the location string."""
    if not location or location.lower() == 'n/a':
        return ""
    # Take the part before the first comma or parenthesis
    match = re.match(r"^([^,(]+)", location)
    if match:
        return match.group(1).strip()
    return location.strip()

def process_sagre(sagre_data: List[Dict[str, Any]]) -> List[Dict[str, Any]]:
    """Cleans, normalizes, and deduplicates a list of sagre events."""
    processed_sagre: Dict[
        Tuple[str, Optional[str], Optional[str], str],
        Dict[str, Any]
    ] = {}
    
    for sagra in sagre_data:
        # 1. Normalize and Clean Data
        primary_location = get_primary_location(sagra.get('location', ''))
        cleaned_sagra_name = clean_name(sagra.get('name', ''), primary_location)
        start_date_info = normalize_date(sagra.get('start_date', ''))
        if not start_date_info:
            continue
        start, end = start_date_info
        if start == end:
            end_date_info = normalize_date(sagra.get('stop_date', ''))
            if not end_date_info:
                continue
            normalized_end_date = end_date_info[0]
        else:
            normalized_end_date = start_date_info[1]
        normalized_start_date = start_date_info[0]

        # Skip events that are clearly invalid after cleaning
        if not cleaned_sagra_name or not normalized_start_date or not normalized_end_date or not primary_location:
            continue

        # 2. Create a unique key for deduplication
        unique_key = (cleaned_sagra_name, normalized_start_date, normalized_end_date, primary_location)

        # 3. Deduplicate
        if unique_key not in processed_sagre:
            # If it's a new event, add it to our dictionary
            sagra['cleaned_name'] = cleaned_sagra_name
            sagra['normalized_start_date'] = normalized_start_date
            sagra['normalized_end_date'] = normalized_end_date
            sagra['primary_location'] = primary_location
            processed_sagre[unique_key] = sagra
        else:
            # If duplicate, we could merge data, but for now we just keep the first one.
            existing_food = set(processed_sagre[unique_key].get('food_list', []))
            new_food = set(sagra.get('food_list', []))
            combined_food = list(existing_food.union(new_food))
            processed_sagre[unique_key]['food_list'] = combined_food

    return list(processed_sagre.values())

def main():
    parser = argparse.ArgumentParser(description="Clean and deduplicate sagre JSON data.")
    parser.add_argument("input_file", type=str, help="Path to the input JSON file.")
    parser.add_argument("-o", "--output_file", type=str, help="Path to the output JSON file.")
    args = parser.parse_args()

    print(f"Loading data from {args.input_file}...")
    try:
        with open(args.input_file, 'r', encoding='utf-8') as f:
            sagre_data = json.load(f)
    except (IOError, json.JSONDecodeError) as e:
        print(f"Error loading file: {e}")
        return

    print(f"Loaded {len(sagre_data)} events. Processing...")
    cleaned_data = process_sagre(sagre_data)
    print(f"Processing complete. Found {len(cleaned_data)} unique events.")

    # Determine output filename
    if args.output_file:
        output_filename = args.output_file
    else:
        base, ext = os.path.splitext(args.input_file)
        output_filename = f"{base}_cleaned{ext}"

    print(f"Saving cleaned data to {output_filename}...")
    try:
        with open(output_filename, 'w', encoding='utf-8') as f:
            json.dump(cleaned_data, f, ensure_ascii=False, indent=4)
        print("Save complete.")
    except IOError as e:
        print(f"Error saving file: {e}")

if __name__ == "__main__":
    main()
