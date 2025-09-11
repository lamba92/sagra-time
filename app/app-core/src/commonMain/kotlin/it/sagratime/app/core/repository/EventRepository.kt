package it.sagratime.app.core.repository

import it.sagratime.core.data.Event
import it.sagratime.core.data.EventSearchQuery
import it.sagratime.core.data.Locale
import kotlinx.serialization.Serializable

interface EventRepository {
    suspend fun getEventStatistics(): SagraStatistics

    suspend fun getPopularSearches(locale: Locale): List<String>

    suspend fun searchCompletionQuery(
        query: String,
        locale: Locale,
    ): List<String>

    suspend fun search(query: EventSearchQuery): List<Event>
}

@Serializable
data class SagraStatistics(
    val activeEvents: Int,
    val eventsThisYear: Int,
)
