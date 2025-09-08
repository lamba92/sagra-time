package it.sagratime.core.data

import it.sagratime.core.datetime.ZonedDate
import it.sagratime.core.units.Length
import kotlinx.serialization.Serializable

@Serializable
data class SearchEventQuery(
    val queryString: String,
    val location: GeoCoordinates,
    val radius: Length,
    val types: Set<EventType>,
    val from: ZonedDate,
    val to: ZonedDate,
    val locale: Locale,
)
