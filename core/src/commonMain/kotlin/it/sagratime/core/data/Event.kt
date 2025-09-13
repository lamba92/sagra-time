package it.sagratime.core.data

import it.sagratime.core.datetime.ZonedDateTime
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
enum class EventType {
    Sagra,
    TownFestival,
    Other,
}

@Serializable
@JvmInline
value class EventId(
    val id: String,
)

@Serializable
data class Event(
    val id: EventId,
    val imageUrl: String? = null,
    val type: EventType,
    val name: String,
    val food: List<String>,
    val from: ZonedDateTime,
    val until: ZonedDateTime,
    val sourceLinks: List<String>,
    val description: String? = null,
    val location: Location,
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
