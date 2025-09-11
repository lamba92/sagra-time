package it.sagratime.core.data

import kotlinx.serialization.Serializable

@Serializable
data class EventsStatistics(
    val activeEvents: Int,
    val eventsThisYear: Int,
)
