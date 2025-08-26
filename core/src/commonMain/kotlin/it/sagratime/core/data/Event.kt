package it.sagratime.core.data

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
enum class EventType {
    Sagra,
    TownFestival,
}

@Serializable
@JvmInline
value class SagraId(
    val id: String,
)

@Serializable
data class Event(
    val id: SagraId,
    val type: EventType,
    val name: String,
    val food: List<String>,
    val from: LocalDateTime,
    val until: LocalDateTime,
    val description: String? = null,
    val location: Location,
)

@Serializable
data class GeoCoordinates(
    val latitude: Double,
    val longitude: Double,
)

@Serializable
data class Location(
    val geoCoordinates: GeoCoordinates,
    val cityName: String,
    val region: ItalianRegion,
)

@Serializable
enum class ItalianRegion {
    Abruzzo,
    Basilicata,
    Calabria,
    Campania,
    EmiliaRomagna,
    FriuliVeneziaGiulia,
    Lazio,
    Liguria,
    Lombardia,
    Marche,
    Molise,
    Piemonte,
    Puglia,
    Sardegna,
    Sicilia,
    Toscana,
    TrentinoAltoAdige,
    Umbria,
    ValledAosta,
    Veneto,
}
