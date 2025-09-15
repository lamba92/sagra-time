package it.sagratime.core.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.seconds

actual class SystemLocaleProvider(
    scope: CoroutineScope,
) {
    companion object {
        val DEFAULT_LOCALE: Locale
            get() {
                val system = java.util.Locale.getDefault()
                return Locale(
                    languageIdentifier = system.language,
                    regionIdentifier = system.country,
                )
            }
    }

    actual val currentLocale: StateFlow<Locale> =
        flow {
            while (true) {
                emit(DEFAULT_LOCALE)
                delay(1.seconds)
            }
        }.stateIn(
            scope = scope,
            started = SharingStarted.Companion.Eagerly,
            initialValue = DEFAULT_LOCALE,
        )
}
