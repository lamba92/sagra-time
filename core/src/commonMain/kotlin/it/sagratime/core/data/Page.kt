package it.sagratime.core.data

import kotlinx.serialization.Serializable

@Serializable
data class Page<T>(
    val currentPage: Int,
    val totalPages: Int,
    val itemsPerPage: Int,
    val results: List<T>,
)
