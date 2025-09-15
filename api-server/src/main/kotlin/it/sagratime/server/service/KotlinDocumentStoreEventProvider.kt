@file:OptIn(ExperimentalTime::class)

package it.sagratime.server.service

import com.github.lamba92.kotlin.document.store.core.ObjectCollection
import it.sagratime.core.data.Event
import it.sagratime.core.data.EventSearchQuery
import it.sagratime.core.data.EventsStatistics
import it.sagratime.core.data.Locale
import it.sagratime.core.data.Page
import it.sagratime.core.data.SearchCompletionQuery
import it.sagratime.core.datetime.compareTo
import it.sagratime.core.datetime.toZonedDateTime
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import kotlinx.datetime.TimeZone
import kotlin.math.max
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

class KotlinDocumentStoreEventProvider(
    private val collection: ObjectCollection<Event>,
) : V1ServerEventRepository {
    override suspend fun getEventStatistics(): EventsStatistics {
        val now = Clock.System.now()
        val currentDate = now.toZonedDateTime(TimeZone.currentSystemDefault())
        val nextYear = now.plus(365.days).toZonedDateTime(TimeZone.currentSystemDefault())
        var activeEvents = 0
        var eventInNext365Days = 0

        collection
            .iterateAll()
            .collect {
                val isActive = it.from >= currentDate && it.until <= currentDate
                if (isActive) {
                    activeEvents++
                }
                if (it.until >= currentDate || it.from <= nextYear) {
                    eventInNext365Days++
                }
            }

        return EventsStatistics(
            activeEvents = activeEvents,
            eventsThisYear = eventInNext365Days,
        )
    }

    override suspend fun getPopularSearches(locale: Locale): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun searchCompletion(query: SearchCompletionQuery): List<String> {
        TODO("Not yet implemented")
    }

    override suspend fun search(query: EventSearchQuery): Page<Event> {
        val results =
            collection
                .iterateAll()
                .filter { sagra -> query.queryString?.let { sagra.matchesQuery(it) } ?: true }
                .filter { sagra -> query.dateRange?.from?.let { sagra.from > it } ?: true }
                .filter { sagra ->
                    query.location?.let { it.from.haversineDistance(sagra.location.geoCoordinates) <= it.radius }
                        ?: true
                }.toList()

        val offset = query.page * query.size

        return Page(
            currentPage = query.page,
            totalPages = max((results.size - 1) / query.size + 1, 0),
            results = results.subList(offset, minOf(offset + query.size, results.size)),
            itemsPerPage = query.size,
        )
    }

    override suspend fun addSagra(event: Event) {
        collection.insert(event)
    }
}
