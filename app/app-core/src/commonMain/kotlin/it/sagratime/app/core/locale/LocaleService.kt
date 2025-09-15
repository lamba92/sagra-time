@file:OptIn(ExperimentalCoroutinesApi::class)

package it.sagratime.app.core.locale

import it.sagratime.core.data.Locale
import it.sagratime.core.data.SystemLocaleProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class LocaleService(
    systemLocaleProvider: SystemLocaleProvider,
    serviceScope: CoroutineScope,
    initial: LocaleSelection = LocaleSelection.System,
) {
    private val localeSelectionState: MutableStateFlow<LocaleSelection> = MutableStateFlow(initial)

    val currentLocale: StateFlow<Locale> =
        localeSelectionState
            .flatMapLatest { selection ->
                when (selection) {
                    is LocaleSelection.Custom -> flowOf(selection.locale)
                    LocaleSelection.System -> systemLocaleProvider.currentLocale
                }
            }.stateIn(
                scope = serviceScope,
                started = Eagerly,
                initialValue =
                    when (initial) {
                        is LocaleSelection.Custom -> initial.locale
                        LocaleSelection.System -> systemLocaleProvider.currentLocale.value
                    },
            )

    val selectedLocaleType: StateFlow<SelectedLocaleType> =
        localeSelectionState
            .map { selection ->
                when (selection) {
                    is LocaleSelection.Custom -> SelectedLocaleType.Custom
                    LocaleSelection.System -> SelectedLocaleType.System
                }
            }.stateIn(
                scope = serviceScope,
                started = Eagerly,
                initialValue =
                    when (initial) {
                        is LocaleSelection.Custom -> SelectedLocaleType.Custom
                        LocaleSelection.System -> SelectedLocaleType.System
                    },
            )

    fun setLocale(locale: LocaleSelection) {
        localeSelectionState.value = locale
    }
}
