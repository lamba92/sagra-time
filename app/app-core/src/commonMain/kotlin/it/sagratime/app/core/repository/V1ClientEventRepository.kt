package it.sagratime.app.core.repository

import it.sagratime.core.repository.V1EventRepository

interface V1ClientEventRepository : V1EventRepository {
    val endpoints: EventRepositoryV1Endpoints
}
