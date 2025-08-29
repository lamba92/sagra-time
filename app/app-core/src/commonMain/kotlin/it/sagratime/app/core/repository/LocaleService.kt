package it.sagratime.app.core.repository

import it.sagratime.core.data.Locale
import kotlinx.coroutines.flow.MutableStateFlow

class LocaleService {
    val currentLocale = MutableStateFlow(Locale.Companion.IT)
}
