package it.sagratime.server.service

import it.sagratime.core.data.Event
import it.sagratime.core.data.Page
import kotlinx.datetime.LocalDateTime

interface SagraProvider {

    suspend fun search(
        page: Int,
        size: Int,
        searchQuery: String? = null,
        locationQuery: LocationQuery? = null,
        from: LocalDateTime? = null,
    ): Page<Event>

    suspend fun addSagra(event: Event)
}

