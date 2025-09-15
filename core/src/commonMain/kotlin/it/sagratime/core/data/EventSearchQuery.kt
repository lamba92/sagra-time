@file:OptIn(ExperimentalSerializationApi::class)

package it.sagratime.core.data

import it.sagratime.core.datetime.ZonedDate
import it.sagratime.core.units.Length
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class EventSearchQuery(
    val page: Int = 0,
    val size: Int = 25,
    val queryString: String? = null,
    val location: LocationQuery? = null,
    val types: Set<EventType>? = null,
    val dateRange: DateRange? = null,
    val locale: Locale = Locale.IT,
)

@Serializable
data class DateRange(
    val from: ZonedDate,
    val to: ZonedDate? = null,
)

@Serializable
data class LocationQuery(
    val from: GeoCoordinates,
    val radius: Length,
)

@Serializable
data class SearchCompletionQuery(
    val query: String,
    val locale: Locale,
)
