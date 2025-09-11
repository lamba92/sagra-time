package it.sagratime.server.service

import com.github.lamba92.kotlin.document.store.core.ObjectCollection
import it.sagratime.core.data.Event
import it.sagratime.core.data.EventSearchQuery
import it.sagratime.core.data.Page
import it.sagratime.core.datetime.compareTo
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.toList
import kotlin.math.max

class KotlinDocumentStoreEventProvider(
    private val collection: ObjectCollection<Event>,
) : EventProvider {
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
