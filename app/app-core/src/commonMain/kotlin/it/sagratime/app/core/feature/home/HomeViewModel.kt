package it.sagratime.app.core.feature.home

import it.sagratime.app.core.MVIViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : MVIViewModel<HomeState, HomeEffect, HomeEvent>() {
    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(TODO())
    override val state: StateFlow<HomeState> = _state.asStateFlow()

    override fun onEvent(event: HomeEvent) {
        when (event) {
            else -> TODO()
        }
    }
}
