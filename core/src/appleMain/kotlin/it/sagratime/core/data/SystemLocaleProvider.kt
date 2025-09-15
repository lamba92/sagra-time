package it.sagratime.core.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import platform.Foundation.NSCurrentLocaleDidChangeNotification
import platform.Foundation.NSLocale
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.Foundation.autoupdatingCurrentLocale
import platform.Foundation.languageCode
import platform.Foundation.preferredLanguages
import platform.Foundation.regionCode

actual class SystemLocaleProvider(
    scope: CoroutineScope,
) {
    companion object {
        val DEFAULT_LOCALE: Locale
            get() {
                val locale = NSLocale.autoupdatingCurrentLocale()
                return Locale(
                    languageIdentifier =
                        NSLocale
                            .preferredLanguages
                            .firstOrNull() as String?
                            ?: locale.languageCode,
                    regionIdentifier = locale.regionCode,
                )
            }
    }

    actual val currentLocale: StateFlow<Locale> =
        callbackFlow {
            val listener =
                NSNotificationCenter.defaultCenter.addObserverForName(
                    name = NSCurrentLocaleDidChangeNotification,
                    `object` = null,
                    queue = NSOperationQueue.mainQueue,
                ) { _ -> trySend(DEFAULT_LOCALE) }
            awaitClose { NSNotificationCenter.defaultCenter.removeObserver(listener) }
        }.stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = DEFAULT_LOCALE,
        )
}
