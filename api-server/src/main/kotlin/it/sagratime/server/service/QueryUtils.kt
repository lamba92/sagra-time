package it.sagratime.server.service

import it.sagratime.core.data.Event
import it.sagratime.core.data.GeoCoordinates
import it.sagratime.core.units.Length
import it.sagratime.core.units.kilometers
import kotlinx.serialization.Serializable
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

val EarthRadius
    get() = 6371.0 // Earth radius in km

fun GeoCoordinates.haversineDistance(point: GeoCoordinates): Length {
    val phi1 = Math.toRadians(latitude)
    val phi2 = Math.toRadians(point.latitude)
    val dPhi = Math.toRadians(point.latitude - latitude)
    val dLambda = Math.toRadians(point.latitude - latitude)

    val a = sin(dPhi / 2).pow(2) + cos(phi1) * cos(phi2) * sin(dLambda / 2).pow(2)
    val c = 2 * asin(sqrt(a))

    return (EarthRadius * c).kilometers
}

fun Event.matchesQuery(query: String): Boolean =
    description?.contains(query, true) == true ||
        food.any { it.contains(query, true) } ||
        name.contains(query, true) ||
        location.region.name.contains(query, true) ||
        location.cityName.contains(query, true)

@Serializable
data class LocationQuery(
    val from: GeoCoordinates,
    val radius: Length,
)
