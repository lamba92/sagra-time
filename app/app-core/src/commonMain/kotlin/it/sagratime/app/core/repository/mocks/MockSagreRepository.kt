package it.sagratime.app.core.repository.mocks

import it.sagratime.app.core.repository.SagraStatistics
import it.sagratime.app.core.repository.SagreRepository
import it.sagratime.core.data.Locale
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

object MockSagreRepository : SagreRepository {
    override suspend fun getSagraStatistics(): SagraStatistics {
        delay(Random.Default.nextInt(4, 10).seconds)
        return SagraStatistics(
            activeEvents = 10,
            eventsThisYear = 100,
        )
    }

    override suspend fun getPopularSearches(locale: Locale): List<String> {
        delay(Random.Default.nextInt(4, 10).seconds)
        return listOf(
            "Pizza fritta",
            "Arrosticini",
            "Vino",
            "Castagne",
        )
    }

    override suspend fun searchCompletionQuery(
        query: String,
        locale: Locale,
    ): List<String> {
        delay(Random.Default.nextInt(1, 2).seconds)
        return listOf(
            "Pizza fritta",
            "Arrosticini",
            "Vino",
            "Castagne",
        )
    }
}
