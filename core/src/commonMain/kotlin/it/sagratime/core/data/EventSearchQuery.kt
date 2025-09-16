@file:OptIn(ExperimentalSerializationApi::class)

package it.sagratime.core.data

import it.sagratime.core.datetime.ZonedDate
import it.sagratime.core.units.Length
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventSearchQuery(
    @SerialName("p")
    val page: Int = 0,
    @SerialName("s")
    val size: Int = 25,
    @SerialName("q")
    val queryString: String? = null,
    @SerialName("l")
    val location: LocationQuery? = null,
    @SerialName("t")
    val types: Set<EventType>? = null,
    @SerialName("d")
    val dateRange: DateRange? = null,
    @SerialName("lang")
    val locale: Locale = Locale.IT,
)

@Serializable
data class DateRange(
    @SerialName("f")
    val from: ZonedDate,
    @SerialName("t")
    val to: ZonedDate? = null,
)

@Serializable
data class LocationQuery(
    @SerialName("f")
    val from: GeoCoordinates,
    @SerialName("r")
    val radius: Length,
)

@Serializable
data class SearchCompletionQuery(
    @SerialName("q")
    val query: String,
    @SerialName("l")
    val locale: Locale,
)
