package it.sagratime.app.core.locale

import it.sagratime.core.data.Locale
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
sealed interface LocaleSelection {
    @Serializable
    object System : LocaleSelection

    @Serializable
    @JvmInline
    value class Custom(
        val locale: Locale,
    ) : LocaleSelection
}
