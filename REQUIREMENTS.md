# REQUIREMENTS for SagraTime
SagraTime is a bot for searching for info about Sagre in Italy and serving them via API and a web app.

## Search
- by name
- by food
- filter by date
- by distance from location

## Data
- location
  - city
  - region
  - lat/long
- food
- when
  - start/end dates (and possibly time)
- small description
- link to official whatever
- tag (not right now)
  - let ai search already existing ones and let it decide if to create new ones
- type:
  - gastro
  - eno
  - agricola
  - folklore
  - marinara
  - festa paese

## Infra Architecture
- embedded database for storage
- server for APIs
- Compose SPA
    - generate Google sitemap xml
    - add hidden SSR for SEO

## Crawler
- weekly job
- search with a default query for each Italian region
- crawl mix a fixed depth
- render JS in page if needed
- use LLM to extract data from document
- serialize data in JSON


## Ideas
- account
  - personal calendar
- tinder like sagra selection
  - requires an account
- Ai calendar
- i18n
- 