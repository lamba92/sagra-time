package it.sagratime.app.core.repository

import it.sagratime.core.data.Locale
import kotlinx.serialization.Serializable

interface SagreRepository {
    suspend fun getSagraStatistics(): SagraStatistics

    suspend fun getPopularSearches(locale: Locale): List<String>

    suspend fun searchCompletionQuery(
        query: String,
        locale: Locale,
    ): List<String>
}

@Serializable
data class SagraStatistics(
    val activeEvents: Int,
    val eventsThisYear: Int,
)
