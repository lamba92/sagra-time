@file:OptIn(ExperimentalCoroutinesApi::class)

package it.sagratime.app.core.feature.home

import androidx.lifecycle.viewModelScope
import it.sagratime.app.core.MVIViewModel
import it.sagratime.app.core.feature.cards.search.SearchCardEffect
import it.sagratime.app.core.repository.V1EventRepository
import it.sagratime.core.data.EventSearchQuery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeViewModel(
    private val eventRepository: V1EventRepository,
) : MVIViewModel<HomeState, HomeEffect, HomeEvent>() {
    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState.Default)
    override val state: StateFlow<HomeState> = _state.asStateFlow()

    private val searchQueue = Channel<EventSearchQuery>()

    init {
        searchQueue
            .consumeAsFlow()
            .mapLatest { eventRepository.search(it) }
            .onEach { _state.value = HomeState.Searching.Success(it) }
            .launchIn(viewModelScope)
    }

    override fun onEvent(event: HomeEvent) {
        viewModelScope.launch {
            when (event) {
                HomeEvent.TopBarTitleClick ->
                    _state.value = HomeState.Default

                is HomeEvent.SearchEffect ->
                    when (event.effect) {
                        SearchCardEffect.NotifyLocationServicesDisabled ->
                            _effects.emit(HomeEffect.NotifyLocationServicesDisabled)

                        is SearchCardEffect.Search ->
                            initiateSearch(event.effect.query)
                    }
            }
        }
    }

    private suspend fun initiateSearch(query: EventSearchQuery) {
        _state.value = HomeState.Searching.Loading
        searchQueue.send(query)
    }
}
