package it.sagratime.core.data

import kotlinx.serialization.Serializable

@Serializable
data class Locale(
    val languageIdentifier: String,
    val regionIdentifier: String? = null,
) {
    companion object {
        val IT = Locale("it")
        val EN = Locale("en")
        val DE = Locale("de")
        val FR = Locale("fr")
        val ES = Locale("es")
    }
}
