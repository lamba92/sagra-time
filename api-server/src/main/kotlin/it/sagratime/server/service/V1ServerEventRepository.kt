package it.sagratime.server.service

import it.sagratime.core.data.Event
import it.sagratime.core.repository.V1EventRepository

interface V1ServerEventRepository : V1EventRepository {
    suspend fun addSagra(event: Event)
}
