@file:OptIn(ExperimentalTime::class)

package it.sagratime.app.core.repository.mocks

import it.sagratime.app.core.repository.EventRepositoryV1Endpoints
import it.sagratime.app.core.repository.V1EventRepository
import it.sagratime.core.data.Event
import it.sagratime.core.data.EventId
import it.sagratime.core.data.EventSearchQuery
import it.sagratime.core.data.EventType
import it.sagratime.core.data.EventsStatistics
import it.sagratime.core.data.GeoCoordinates
import it.sagratime.core.data.ItalianRegion
import it.sagratime.core.data.Locale
import it.sagratime.core.data.Location
import it.sagratime.core.datetime.toZonedDateTime
import kotlinx.coroutines.delay
import kotlinx.datetime.TimeZone
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

object MockEventRepository : V1EventRepository {
    private val random = Random(0)
    override val endpoints: EventRepositoryV1Endpoints
        get() = EventRepositoryV1Endpoints.LOCALHOST

    override suspend fun getEventStatistics(): EventsStatistics {
        delay(Random.Default.nextInt(4, 10).seconds)
        return EventsStatistics(
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

    override suspend fun search(query: EventSearchQuery) =
        List(10) {
            val from = Clock.System.now()
            val until = from + Random.Default.nextInt(0, 3).days
            Event(
                id = EventId(it.toString()),
                imageUrl = null,
                type = EventType.entries.random(random),
                name = "Event $it",
                food = List(random.nextInt(1, 5)) { "Food $it" },
                from = from.toZonedDateTime(TimeZone.currentSystemDefault()),
                until = until.toZonedDateTime(TimeZone.currentSystemDefault()),
                sourceLinks = emptyList(),
                description =
                    "Immergiti nella cultura vinicola toscana con degustazioni guidate, " +
                        "visite alle cantine storiche e abbinamenti con prodotti tipici del territorio " +
                        "chiantigiano.",
                location =
                    Location(
                        geoCoordinates = GeoCoordinates(0.0, 0.0),
                        cityName = "City $it",
                        region = ItalianRegion.entries.random(random),
                    ),
            )
        }
}
