package it.sagrabot.core.data

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Sagra(
    val name: String,
    val food: List<String>,
    val from: LocalDateTime,
    val until: LocalDateTime,
    val description: String? = null,
    val location: Location,
)

@Serializable
data class Location(
    val lat: Double,
    val lon: Double,
    val cityName: String,
    val region: ItalianRegion,
)

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
