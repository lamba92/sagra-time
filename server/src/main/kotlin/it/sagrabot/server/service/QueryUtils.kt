package it.sagrabot.server.service

import it.sagrabot.core.data.Coordinates
import it.sagrabot.core.data.Sagra
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.text.contains
import kotlinx.serialization.Serializable

val EarthRadius
    get() = 6371.0 // Earth radius in km

fun Coordinates.haversineDistanceInKmTo(point: Coordinates): Double {

    val phi1 = Math.toRadians(lat)
    val phi2 = Math.toRadians(point.lat)
    val dPhi = Math.toRadians(point.lat - lat)
    val dLambda = Math.toRadians(point.lat - lat)

    val a = sin(dPhi / 2).pow(2) + cos(phi1) * cos(phi2) * sin(dLambda / 2).pow(2)
    val c = 2 * asin(sqrt(a))

    return EarthRadius * c
}

fun Sagra.matchesQuery(query: String): Boolean =
    description?.contains(query, true) == true
            || food.any { it.contains(query, true) }
            || name.contains(query, true)
            || location.region.name.contains(query, true)
            || location.cityName.contains(query, true)

@Serializable
data class LocationQuery(val from: Coordinates, val radiusInKm: Int)