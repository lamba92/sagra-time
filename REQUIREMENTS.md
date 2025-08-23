# REQUIREMENTS for SagraBot
Sagrabot is a bot for searching for info about Sagre in Italy and serving them via API and a web app.

## Search
- by name
- by food
- filter by date
- by distance from location

## Data
- location
- food
- date
- small description
- link to official whatever

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