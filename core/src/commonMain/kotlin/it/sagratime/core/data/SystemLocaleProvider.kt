package it.sagratime.core.data

import kotlinx.coroutines.flow.StateFlow

expect class SystemLocaleProvider {
    val currentLocale: StateFlow<Locale>
}
