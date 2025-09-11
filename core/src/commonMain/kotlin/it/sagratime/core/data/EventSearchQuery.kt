@file:OptIn(ExperimentalSerializationApi::class)

package it.sagratime.core.data

import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder
import io.ktor.util.appendAll
import io.ktor.util.toMap
import it.sagratime.core.datetime.ZonedDate
import it.sagratime.core.units.Length
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.properties.Properties
import kotlinx.serialization.properties.decodeFromStringMap
import kotlinx.serialization.properties.encodeToStringMap

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

fun EventSearchQuery.toQueryParams(properties: Properties = Properties) =
    buildQueryParams {
        appendAll(properties.encodeToStringMap(this@toQueryParams))
    }

fun Parameters.toEventSearchQuery(properties: Properties = Properties): EventSearchQuery {
    val toMap = toMap()
    val map = toMap.mapValues { it.value.joinToString(",") }
    val decodeFromStringMap = properties.decodeFromStringMap<EventSearchQuery>(map)
    return decodeFromStringMap
}

fun buildQueryParams(block: ParametersBuilder.() -> Unit) = ParametersBuilder().apply(block).build()
