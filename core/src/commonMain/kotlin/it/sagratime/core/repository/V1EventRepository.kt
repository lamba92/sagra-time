package it.sagratime.core.repository

import it.sagratime.core.data.Event
import it.sagratime.core.data.EventSearchQuery
import it.sagratime.core.data.EventsStatistics
import it.sagratime.core.data.Locale
import it.sagratime.core.data.Page
import it.sagratime.core.data.SearchCompletionQuery

interface V1EventRepository {
    suspend fun getEventStatistics(): EventsStatistics

    suspend fun getPopularSearches(locale: Locale): List<String>

    suspend fun searchCompletion(query: SearchCompletionQuery): List<String>

    suspend fun search(query: EventSearchQuery): Page<Event>
}
