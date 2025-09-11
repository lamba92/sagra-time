package it.sagratime.server.service

import it.sagratime.core.data.Event
import it.sagratime.core.data.EventSearchQuery
import it.sagratime.core.data.Page

interface EventProvider {
    suspend fun search(query: EventSearchQuery): Page<Event>

    suspend fun addSagra(event: Event)
}
