package it.sagratime.app.core.repository

import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.seconds

interface SagreRepository {
    suspend fun getSagraStatistics(): SagraStatistics
}

@Serializable
data class SagraStatistics(
    val activeEvents: Int,
    val eventsThisYear: Int,
)

object MockSagreRepository : SagreRepository {
    override suspend fun getSagraStatistics(): SagraStatistics {
        delay(5.seconds)
        return SagraStatistics(
            activeEvents = 10,
            eventsThisYear = 100,
        )
    }
}
