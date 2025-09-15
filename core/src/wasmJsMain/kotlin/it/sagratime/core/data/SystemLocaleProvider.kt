package it.sagratime.core.data

import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import org.w3c.dom.events.Event

actual class SystemLocaleProvider(
    coroutineScope: CoroutineScope,
) {
    companion object {
        val SYSTEM_DEFAULT: Locale
            get() {
                val language = window.navigator.language
                val lang = language.substringBefore("-")
                val region =
                    language
                        .substringAfter('-', "")
                        .ifBlank { null }
                return Locale(
                    languageIdentifier = lang,
                    regionIdentifier = region,
                )
            }
    }

    actual val currentLocale: StateFlow<Locale> =
        callbackFlow {
            val handler: (Event) -> Unit = { trySend(SYSTEM_DEFAULT) }
            window.addEventListener("languagechange", handler)
            awaitClose { window.removeEventListener("languagechange", handler) }
        }.stateIn(
            scope = coroutineScope,
            started = SharingStarted.Companion.Eagerly,
            initialValue = SYSTEM_DEFAULT,
        )
}
