package it.sagratime.core.data

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Locale(
    val value: String,
) {
    companion object {
        val IT = Locale("it")
        val EN = Locale("en")
        val DE = Locale("de")
        val FR = Locale("fr")
        val ES = Locale("es")
    }
}
