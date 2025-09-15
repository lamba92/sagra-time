package it.sagratime.app.core.repository

import it.sagratime.core.data.Event
import it.sagratime.core.data.EventSearchQuery
import it.sagratime.core.data.EventsStatistics
import it.sagratime.core.data.Locale

interface V1EventRepository {
    val endpoints: EventRepositoryV1Endpoints

    suspend fun getEventStatistics(): EventsStatistics

    suspend fun getPopularSearches(locale: Locale): List<String>

    suspend fun searchCompletionQuery(
        query: String,
        locale: Locale,
    ): List<String>

    suspend fun search(query: EventSearchQuery): List<Event>
}
