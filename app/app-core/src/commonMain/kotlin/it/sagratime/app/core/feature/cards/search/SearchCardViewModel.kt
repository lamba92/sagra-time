package it.sagratime.app.core.feature.cards.search

import androidx.lifecycle.viewModelScope
import it.sagratime.app.core.MVIViewModel
import it.sagratime.app.core.repository.LocaleService
import it.sagratime.app.core.repository.LocationService
import it.sagratime.app.core.repository.LocationServiceStatus
import it.sagratime.app.core.repository.SagreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchCardViewModel(
    private val sagreRepository: SagreRepository,
    localeService: LocaleService,
    private val locationService: LocationService,
) : MVIViewModel<SearchCardState, SearchCardEffect, SearchCardEvent>() {
    private val _state: MutableStateFlow<SearchCardState> =
        MutableStateFlow(SearchCardState.DEFAULT)
    override val state: StateFlow<SearchCardState> = _state.asStateFlow()

    init {
        localeService
            .currentLocale
            .map { locale -> sagreRepository.getPopularSearches(locale) }
            .map { searches -> SearchCardState.PopularSearches.Loaded(searches) }
            .map { searches -> _state.update { it.copy(popularSearches = searches) } }
            .launchIn(viewModelScope)

        combine(
            state.map { it.aroundMe },
            locationService.status,
        ) { aroundMe, locationServiceStatus ->
            when {
                locationServiceStatus == LocationServiceStatus.Disabled ->
                    SearchCardState.AroundMe.LocationServicesDisabled

                aroundMe == SearchCardState.AroundMe.Loading && locationServiceStatus is LocationServiceStatus.Active ->
                    SearchCardState.AroundMe.Selected

                else -> aroundMe
            }
        }.onEach { aroundMe -> _state.update { it.copy(aroundMe = aroundMe) } }
            .launchIn(viewModelScope)
    }

    override fun onEvent(event: SearchCardEvent) {
        when (event) {
            SearchCardEvent.ClearClicked ->
                _state.value = SearchCardState.DEFAULT

            is SearchCardEvent.DateSelectionChanged ->
                _state.update { it.copy(dateSelection = event.dateSelection) }

            is SearchCardEvent.QueryChanged ->
                _state.update { it.copy(query = event.query) }

            SearchCardEvent.SearchClicked -> TODO()

            is SearchCardEvent.SearchRadiusChanged ->
                _state.update { it.copy(searchRadius = event.searchRadius) }

            is SearchCardEvent.SelectedTypesChanged.TypeAdded ->
                _state.update { it.copy(selectedTypes = _state.value.selectedTypes + event.type) }

            is SearchCardEvent.SelectedTypesChanged.TypeRemoved ->
                _state.update { it.copy(selectedTypes = _state.value.selectedTypes - event.type) }

            SearchCardEvent.AdvancedSearchClicked ->
                _state.update { it.copy(isAdvancedSearch = true) }

            SearchCardEvent.AdvancedSearchDismissed ->
                _state.update { it.copy(isAdvancedSearch = false) }

            SearchCardEvent.AroundMeClicked ->
                updateAroundMeState()

            is SearchCardEvent.PopularSearchClick ->
                _state.update { it.copy(query = event.query) }
        }
    }

    private fun updateAroundMeState() {
        when (state.value.aroundMe) {
            SearchCardState.AroundMe.Unselected -> {
                viewModelScope.launch { locationService.requestLocation() }
                _state.update { it.copy(aroundMe = SearchCardState.AroundMe.Loading) }
            }

            SearchCardState.AroundMe.Selected ->
                _state.update { it.copy(aroundMe = SearchCardState.AroundMe.Unselected) }

            SearchCardState.AroundMe.LocationServicesDisabled ->
                _effects.tryEmit(SearchCardEffect.NotifyLocationServicesDisabled)

            SearchCardState.AroundMe.Loading -> { // no- op
            }
        }
    }
}
