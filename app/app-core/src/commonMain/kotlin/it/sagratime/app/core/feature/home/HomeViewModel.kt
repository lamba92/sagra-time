package it.sagratime.app.core.feature.home

import it.sagratime.app.core.MVIViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : MVIViewModel<HomeState, HomeEffect, HomeEvent>() {
    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(TODO())
    override val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _effects: MutableSharedFlow<HomeEffect> = MutableSharedFlow()
    override val effects: SharedFlow<HomeEffect> =
        _effects.asSharedFlow()

    override fun onEvent(event: HomeEvent) {
        when (event) {
            else -> TODO()
        }
    }
}
