import argparse
import json
from typing import List, Dict, Any, Optional
from datetime import datetime

from langchain_core.messages import SystemMessage, HumanMessage
from langchain_openai import ChatOpenAI

from sqlalchemy import create_engine, Column, Integer, String, Date, Float, ForeignKey, Table, UniqueConstraint
from sqlalchemy.orm import sessionmaker, relationship, declarative_base, Session


import config
from data_models import FoodList, TagList

DB_FILE = "sagre.db"
DATABASE_URL = f"sqlite:///{DB_FILE}"

# --- SQLAlchemy Setup ---
Base = declarative_base()

occurrence_food_association = Table('occurrence_foods', Base.metadata,
    Column('occurrence_id', Integer, ForeignKey('occurrences.id', ondelete="CASCADE"), primary_key=True),
    Column('food_id', Integer, ForeignKey('foods.id', ondelete="CASCADE"), primary_key=True)
)

occurrence_tag_association = Table('occurrence_tags', Base.metadata,
    Column('occurrence_id', Integer, ForeignKey('occurrences.id', ondelete="CASCADE"), primary_key=True),
    Column('tag_id', Integer, ForeignKey('tags.id', ondelete="CASCADE"), primary_key=True)
)

# --- Database Configuration ---
class Event(Base):
    __tablename__ = 'events'
    id = Column(Integer, primary_key=True)
    name = Column(String, nullable=False)
    primary_location = Column(String, nullable=False)
    
    occurrences = relationship("Occurrence", back_populates="event", cascade="all, delete-orphan")
    
    __table_args__ = (UniqueConstraint('name', 'primary_location', name='_name_location_uc'),)

class Occurrence(Base):
    __tablename__ = 'occurrences'
    id = Column(Integer, primary_key=True)
    event_id = Column(Integer, ForeignKey('events.id', ondelete="CASCADE"), nullable=False)
    start_date = Column(Date)
    end_date = Column(Date)
    location_string = Column(String)
    city = Column(String)
    region = Column(String)
    latitude = Column(Float)
    longitude = Column(Float)
    description = Column(String)
    official_url = Column(String)
    source_url = Column(String)
    type = Column(String)
    
    event = relationship("Event", back_populates="occurrences")
    foods = relationship("Food", secondary=occurrence_food_association, back_populates="occurrences")
    tags = relationship("Tag", secondary=occurrence_tag_association, back_populates="occurrences")

class Food(Base):
    __tablename__ = 'foods'
    id = Column(Integer, primary_key=True)
    name = Column(String, nullable=False, unique=True)
    
    occurrences = relationship("Occurrence", secondary=occurrence_food_association, back_populates="foods")

class Tag(Base):
    __tablename__ = 'tags'
    id = Column(Integer, primary_key=True)
    name = Column(String, nullable=False, unique=True)
    
    occurrences = relationship("Occurrence", secondary=occurrence_tag_association, back_populates="tags")

engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

# --- LLM Configuration ---
llm = ChatOpenAI(
    openai_api_base=config.OPENAI_API_BASE,
    openai_api_key=config.OPENAI_API_KEY,
    model_name=config.LLM_MODEL_NAME,
    temperature=0,
)
structured_llm_foods = llm.with_structured_output(FoodList)
structured_llm_tags = llm.with_structured_output(TagList)

def create_database():
    print(f"Initializing database at {DB_FILE}...")
    Base.metadata.create_all(bind=engine)
    print("Database initialized successfully.")

def load_cleaned_data(filepath: str) -> List[Dict[str, Any]]:
    print(f"Loading data from {filepath}...")
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            return json.load(f)
    except (IOError, json.JSONDecodeError) as e:
        print(f"Error loading file: {e}")
        return []

def get_or_create(session: Session, model, **kwargs) -> Any:
    if 'name' in kwargs:
        kwargs['name'] = kwargs['name'].lower()
        
    instance = session.query(model).filter_by(**kwargs).first()
    if instance:
        return instance
    else:
        instance = model(**kwargs)
        session.add(instance)
        session.flush()
        return instance

def get_or_create_event_fuzzy(session: Session, sagra: Dict[str, Any]) -> Optional[Event]:
    event_name = sagra.get('cleaned_name')
    primary_location = sagra.get('primary_location')
    if not event_name or not primary_location:
        return None

    candidates = session.query(Event).filter_by(primary_location=primary_location).all()

    if not candidates:
        print(f"No candidates found for location '{primary_location}'. Creating new event: '{event_name}'")
        return get_or_create(session, Event, name=event_name, primary_location=primary_location)

    candidate_names = [c.name for c in candidates]
    system_prompt = (
        "You are a database entity linking expert. Your task is to determine if a new event is the same as one of a list of existing events that occur in the same town. "
        "Analyze the names. Is the new event the same as one of the existing ones? "
        "Answer with the exact name from the list of existing events if you find a match. If it is a completely new and distinct event, answer with the word 'None'."
    )
    user_prompt = f"""New Event Name: '{event_name}'\n\nExisting Event Names in the same town: {candidate_names}"""

    messages = [SystemMessage(content=system_prompt), HumanMessage(content=user_prompt)]
    response = llm.invoke(messages)
    llm_choice = response.content.strip()

    if llm_choice != 'None' and llm_choice in candidate_names:
        print(f"  LLM linked '{event_name}' to existing event '{llm_choice}'")
        return session.query(Event).filter_by(name=llm_choice, primary_location=primary_location).one()
    
    print(f"  LLM identified '{event_name}' as a new event.")
    return get_or_create(session, Event, name=event_name, primary_location=primary_location)

def normalize_and_link_foods(session: Session, occurrence: Occurrence, food_list: List[str], existing_foods: List[str]):
    if not food_list or all(f.lower() == 'n/a' for f in food_list): return

    system_prompt = (
        "You are an expert at normalizing food data for a database. "
        "Given a JSON list of food names from an Italian food festival ('sagra'), your task is to clean and normalize them into their essential, most basic form. "
        "When normalizing, you should prioritize using a name from the list of already existing food items if a close match is found. This is to maintain consistency in the database. "
        "Also, remove redundant adjectives, regional specifics, and descriptions in parentheses. For example:"
        "- 'salsiccia locale' should become 'salsiccia'. "
        "- 'cavatelli (gnocchetti di semola)' should become 'cavatelli'. "
        "- 'piatti tipici abruzzesi' should become 'piatti tipici'. "
        "If an item is too generic or not a food, exclude it. "
        f"Here is the list of food names already in the database. Prefer these if possible: {existing_foods}"
        "Respond with a single JSON list of the cleaned, essential food names."
    )
    user_prompt = json.dumps(food_list)

    try:
        messages = [SystemMessage(content=system_prompt), HumanMessage(content=user_prompt)]
        response_model = structured_llm_foods.invoke(messages)
        
        if response_model.foods:
            print(f"  LLM normalized foods: {food_list} -> {response_model.foods}")
            for food_name in response_model.foods:
                if not food_name: continue
                food_obj = get_or_create(session, Food, name=food_name)
                if food_obj not in occurrence.foods:
                    occurrence.foods.append(food_obj)
    except Exception as e:
        print(f"  [Error] Failed to normalize foods for occurrence {occurrence.id}: {e}")

def generate_and_link_tags(session: Session, sagra: Dict[str, Any], occurrence: Occurrence, existing_tags: List[str]):
    description = sagra.get('info_summary', '')
    if not description or description.lower() == 'n/a': return

    food_list_str = ", ".join(sagra.get('food_list', []))
    event_name = sagra.get('cleaned_name', '')

    system_prompt = (
        "You are an expert at analyzing event descriptions to extract relevant tags. "
        "Generate a list of short, relevant tags in Italian that describe the event's atmosphere and activities. "
        "When generating tags, you should prioritize using a tag from the list of already existing tags if a close match is found. This is to maintain consistency in the database. "
        "Focus on concepts like 'musica dal vivo', 'bambini', 'cultura', 'rievocazione storica', 'mercatino', 'sport', 'religioso'. "
        "Do NOT include specific food items as I already have them. "
        f"Here is the list of tags already in the database. Prefer these if possible: {existing_tags}"
        "If no such tags apply, return an empty list. Respond with a JSON list of strings, for example: [\"musica dal vivo\", \"mercatino artigianale\"]"
    )
    user_prompt = f"Event Name: '{event_name}'\nDescription: '{description}'\nKnown Food Items: {food_list_str}"

    try:
        messages = [SystemMessage(content=system_prompt), HumanMessage(content=user_prompt)]
        response_model = structured_llm_tags.invoke(messages)

        if response_model.tags:
            print(f"  LLM generated tags: {response_model.tags}")
            for tag_name in response_model.tags:
                if not tag_name: continue
                tag_obj = get_or_create(session, Tag, name=tag_name)
                if tag_obj not in occurrence.tags:
                    occurrence.tags.append(tag_obj)
    except Exception as e:
        print(f"  [Error] Failed to generate tags for '{sagra.get('cleaned_name')}': {e}")

def populate_database(session: Session, sagre_data: List[Dict[str, Any]]):
    existing_foods = [f[0] for f in session.query(Food.name).all()]
    existing_tags = [t[0] for t in session.query(Tag.name).all()]

    for sagra in sagre_data:
        event_obj = get_or_create_event_fuzzy(session, sagra)
        if not event_obj: continue

        start_date_str = sagra.get('normalized_start_date')
        start_date = datetime.strptime(start_date_str, "%Y-%m-%d").date() if start_date_str else None

        duplicate = session.query(Occurrence).filter_by(event_id=event_obj.id, start_date=start_date).first()
        if duplicate:
            print(f"Skipping duplicate occurrence: {event_obj.name} on {start_date_str}")
            continue

        end_date_str = sagra.get('normalized_end_date')
        new_occurrence = Occurrence(
            event=event_obj,
            start_date=start_date,
            end_date=datetime.strptime(end_date_str, "%Y-%m-%d").date() if end_date_str else None,
            location_string=sagra.get('location'),
            city=sagra.get('primary_location'),
            region=sagra.get('region'),
            description=sagra.get('info_summary'),
            official_url=sagra.get('official_url'),
            source_url=sagra.get('source_url'),
            type=sagra.get('type')
        )
        session.add(new_occurrence)
        session.flush()

        normalize_and_link_foods(session, new_occurrence, sagra.get('food_list', []), existing_foods)
        generate_and_link_tags(session, sagra, new_occurrence, existing_tags)
        
        print(f"--- Prepared new occurrence for event '{event_obj.name}' on {start_date_str}")

def main():
    parser = argparse.ArgumentParser(description="Populate the sagre database from a cleaned JSON file.")
    parser.add_argument("input_file", type=str, help="Path to the input cleaned JSON file.")
    args = parser.parse_args()

    create_database()
    sagre_data = load_cleaned_data(args.input_file)
    if not sagre_data:
        print("No data loaded, exiting.")
        return

    print(f"Loaded {len(sagre_data)} cleaned events. Starting database population process...")
    
    session = SessionLocal()
    try:
        populate_database(session, sagre_data)
        print("\nCommitting changes to the database...")
        session.commit()
        print("Database population complete. Changes have been committed.")
    except Exception as e:
        print(f"\nAn error occurred: {e}")
        print("Rolling back changes.")
        session.rollback()
    finally:
        session.close()

if __name__ == "__main__":
    main()