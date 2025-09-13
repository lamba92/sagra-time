package it.sagratime.core.data

import kotlinx.serialization.Serializable

@Serializable
data class GeoCoordinates(
    val lat: Double,
    val lng: Double
)

@Serializable
sealed interface Geometry {

    data class Point(
        val coordinates: GeoCoordinates
    ) : Geometry


    data class Line(
        val geoCoordinates: List<GeoCoordinates>
    ) : Geometry

    data class PolyLine(
        val coordinates: List<Line>
    ) : Geometry

    data class Polygon(
        val geoCoordinates: List<GeoCoordinates>
    ) : Geometry

    data class MultiPolygon( // polygon with extrusions
        val coordinates: List<Polygon>
    ) : Geometry

}

@Serializable
data class GeoJson(
    val id: String,
    val type: Geometry,
    val properties: Map<String, String>
)
