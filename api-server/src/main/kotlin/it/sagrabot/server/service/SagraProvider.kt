package it.sagrabot.server.service

import it.sagrabot.core.data.Page
import it.sagrabot.core.data.Sagra
import kotlinx.datetime.LocalDateTime

interface SagraProvider {

    suspend fun search(
        page: Int,
        size: Int,
        searchQuery: String? = null,
        locationQuery: LocationQuery? = null,
        from: LocalDateTime? = null,
    ): Page<Sagra>

    suspend fun addSagra(sagra: Sagra)
}

