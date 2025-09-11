from typing import List, Literal
from pydantic import BaseModel, Field

class Sagra(BaseModel):
    """Represents a single Italian festival or event."""
    name: str = Field(description="The official name of the event. Example: 'Sagra della Porchetta'")
    start_date: str = Field(description="The start date of the event. Use 'N/A' if not available. Format as YYYY-MM-DD if possible.")
    stop_date: str = Field(description="The end date of the event. Can be the same as start_date. Use 'N/A' if not available. Format as YYYY-MM-DD if possible.")
    location: str = Field(description="The city, town, or specific location of the event. Example: 'Pescara', 'Piazza Salotto, Pescara'")
    source_url: str = Field(description="The source URL from which this event was extracted.")
    food_list: List[str] = Field(description="A list of specific foods or dishes mentioned. Example: ['porchetta', 'vino rosso', 'patate fritte']", default=[])
    info_summary: str = Field(description="A brief summary of the event, including any notable activities or information.", default="N/A")
    official_url: str = Field(description="The official website or social media page for the event. Use 'N/A' if not available.", default="N/A")
    edition_number: str = Field(description="The edition of the event, if mentioned (e.g., '45esima edizione'). Use 'N/A' if not available.", default="N/A")
    type: Literal['sagra', 'festa', 'evento'] = Field(description="The type of the event.")

class SagraList(BaseModel):
    """A list of "sagre" found on a page."""
    sagre: List[Sagra] = Field(description="A list of sagre, feste, or eventi found in the text.")

class FoodList(BaseModel):
    """A list of food found associated to a sagra."""
    foods: List[str] = Field(description="A list of normalized food names, like ['arrosticini', 'porchetta', 'pizza fritta'].")

class TagList(BaseModel):
    """A list of tags found associated to a sagra."""
    tags: List[str] = Field(description="A list of short, relevant tags in Italian, like ['musica dal vivo', 'cultura'].")
