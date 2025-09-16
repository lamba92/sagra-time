package it.sagratime.core.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Locale(
    @SerialName("l")
    val languageIdentifier: String,
    @SerialName("r")
    val regionIdentifier: String? = null,
) {
    companion object {
        val IT = Locale("it", "it")
        val EN = Locale("en", "us")
        val DE = Locale("de", "de")
        val FR = Locale("fr", "fr")
        val ES = Locale("es", "es")
    }
}
