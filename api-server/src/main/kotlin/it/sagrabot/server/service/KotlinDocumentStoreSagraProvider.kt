package it.sagrabot.server.service

import com.github.lamba92.kotlin.document.store.core.ObjectCollection
import it.sagrabot.core.data.Page
import it.sagrabot.core.data.Sagra
import kotlin.math.max
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.datetime.LocalDateTime

class KotlinDocumentStoreSagraProvider(
    private val collection: ObjectCollection<Sagra>
) : SagraProvider {
    override suspend fun search(
        page: Int,
        size: Int,
        searchQuery: String?,
        locationQuery: LocationQuery?,
        from: LocalDateTime?
    ): Page<Sagra> {
        val results =
            collection
                .iterateAll()
                .filter { sagra -> searchQuery?.let { sagra.matchesQuery(it) } ?: true }
                .filter { sagra -> from?.let { sagra.from > it } ?: true }
                .filter { sagra ->
                    locationQuery?.let { it.from.haversineDistanceInKmTo(sagra.location.coordinates) <= it.radiusInKm }
                        ?: true
                }
                .toList()

        val offset = page * size

        return Page(
            currentPage = page,
            totalPages = max((results.size - 1) / size + 1, 0),
            results = results.subList(offset, minOf(offset + size, results.size)),
            itemsPerPage = size
        )
    }

    override suspend fun addSagra(sagra: Sagra) {
        collection.insert(sagra)
    }

}