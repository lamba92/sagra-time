package it.sagratime.core.data

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.LocaleList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.stateIn

actual class SystemLocaleProvider(
    scope: CoroutineScope,
    appContext: Context,
) {
    companion object {
        val DEFAULT_LOCALE: Locale
            get() {
                val top =
                    when {
                        Build.VERSION.SDK_INT >= 24 -> LocaleList.getDefault()[0]
                        else -> java.util.Locale.getDefault()
                    }
                val langId = top.toLanguageTag().ifBlank { top.language.ifBlank { "und" } }
                val regionId = top.country.takeIf { it.isNotBlank() }
                return Locale(
                    languageIdentifier = langId,
                    regionIdentifier = regionId,
                )
            }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    actual val currentLocale: StateFlow<Locale> =
        channelFlow {
            val filter =
                IntentFilter().apply {
                    addAction(Intent.ACTION_LOCALE_CHANGED)
                    addAction(Intent.ACTION_CONFIGURATION_CHANGED)
                }

            val receiver =
                object : BroadcastReceiver() {
                    override fun onReceive(
                        context: Context?,
                        intent: Intent?,
                    ) {
                        trySend(DEFAULT_LOCALE)
                    }
                }

            when {
                Build.VERSION.SDK_INT >= 33 ->
                    appContext.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)

                else -> appContext.registerReceiver(receiver, filter)
            }
            awaitClose { appContext.unregisterReceiver(receiver) }
        }.stateIn(
            scope = scope,
            started = SharingStarted.Companion.Eagerly,
            initialValue = DEFAULT_LOCALE,
        )
}
