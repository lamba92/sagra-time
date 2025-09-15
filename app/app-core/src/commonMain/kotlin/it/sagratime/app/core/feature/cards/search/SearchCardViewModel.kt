@file:OptIn(ExperimentalCoroutinesApi::class)

package it.sagratime.app.core.feature.cards.search

import androidx.lifecycle.viewModelScope
import it.sagratime.app.core.MVIViewModel
import it.sagratime.app.core.combine
import it.sagratime.app.core.locale.LocaleService
import it.sagratime.app.core.repository.LocationService
import it.sagratime.app.core.repository.LocationServiceStatus
import it.sagratime.core.data.DateRange
import it.sagratime.core.data.EventSearchQuery
import it.sagratime.core.data.LocationQuery
import it.sagratime.core.data.SearchCompletionQuery
import it.sagratime.core.repository.V1EventRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchCardViewModel(
    private val eventRepository: V1EventRepository,
    private val localeService: LocaleService,
    private val locationService: LocationService,
) : MVIViewModel<SearchCardState, SearchCardEffect, SearchCardEvent>() {
    private val _state: MutableStateFlow<SearchCardState> =
        MutableStateFlow(SearchCardState.INITIAL)
    override val state: StateFlow<SearchCardState> = _state.asStateFlow()

    private val citiesAutocompleteQueue = Channel<String>()
    private val searchAutocompleteQueue = Channel<String>()

    init {

        citiesAutocompleteQueue
            .consumeAsFlow()
            .combine(localeService.currentLocale)
            .mapLatest { (query, locale) -> locationService.citiesCompletionQuery(query, locale) }
            .onEach { tips ->
                _state.update {
                    it.copy(
                        isLocationQueryTipsLoading = false,
                        locationQueryTips = tips,
                    )
                }
            }.launchIn(viewModelScope)

        searchAutocompleteQueue
            .consumeAsFlow()
            .combine(localeService.currentLocale)
            .mapLatest { (query, locale) ->
                eventRepository.searchCompletion(
                    SearchCompletionQuery(
                        query = query,
                        locale = locale,
                    ),
                )
            }.onEach { tips ->
                _state.update {
                    it.copy(
                        isQueryTipsLoading = false,
                        queryTips = tips,
                    )
                }
            }.launchIn(viewModelScope)

        localeService
            .currentLocale
            .map { locale -> eventRepository.getPopularSearches(locale) }
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

                aroundMe == SearchCardState.AroundMe.Loading &&
                    locationServiceStatus is LocationServiceStatus.Active ->
                    SearchCardState.AroundMe.Selected

                else -> aroundMe
            }
        }.onEach { aroundMe -> _state.update { it.copy(aroundMe = aroundMe) } }
            .launchIn(viewModelScope)
    }

    override fun onEvent(event: SearchCardEvent) {
        viewModelScope.launch {
            when (event) {
                SearchCardEvent.ClearSearchClick ->
                    _state.update { it.clear() }

                is SearchCardEvent.DateRangeSelectionChanged ->
                    _state.update { it.copy(selectedDateRange = event.selection) }

                is SearchCardEvent.QueryChanged -> {
                    searchAutocompleteQueue.send(event.query)
                    _state.update {
                        it.copy(
                            query = event.query,
                            isQueryTipsLoading = true,
                        )
                    }
                }

                SearchCardEvent.SearchClicked ->
                    performSearch()

                is SearchCardEvent.SearchRadiusChanged ->
                    _state.update { it.copy(searchRadius = event.searchRadius) }

                SearchCardEvent.AdvancedSearchClicked ->
                    _state.update { it.copy(isAdvancedSearch = !it.isAdvancedSearch) }

                SearchCardEvent.AdvancedSearchDismissed ->
                    _state.update { it.copy(isAdvancedSearch = false) }

                SearchCardEvent.AroundMeClicked ->
                    updateAroundMeState()

                is SearchCardEvent.PopularSearchClick ->
                    _state.update { it.copy(query = event.query) }

                is SearchCardEvent.SelectedTypesChanged ->
                    _state.update {
                        it.copy(
                            selectedTypes =
                                when (event.type) {
                                    in it.selectedTypes -> it.selectedTypes - event.type
                                    else -> it.selectedTypes + event.type
                                },
                        )
                    }

                SearchCardEvent.ClearLocationSearchClick ->
                    _state.update {
                        it.copy(
                            locationQuery = "",
                            locationQueryTips = emptyList(),
                        )
                    }

                is SearchCardEvent.LocationQueryChanged -> {
                    citiesAutocompleteQueue.send(event.query)
                    _state.update {
                        it.copy(
                            locationQuery = event.query,
                            isLocationQueryTipsLoading = true,
                        )
                    }
                }

                is SearchCardEvent.LocationTipCLick ->
                    _state.update {
                        it.copy(
                            locationQuery = event.location.cityName,
                            selectedLocation = event.location,
                        )
                    }

                is SearchCardEvent.SearchTipClick -> {
                    _state.update { it.copy(query = event.searchTip) }
                }
            }
        }
    }

    private suspend fun performSearch() {
        val currentState = state
        _effects.emit(
            SearchCardEffect.Search(
                EventSearchQuery(
                    queryString = currentState.value.query,
                    location =
                        LocationQuery(
                            from =
                                currentState.value.selectedLocation?.geoCoordinates
                                    ?: locationService.getIpGeoCoordinates(),
                            radius = currentState.value.searchRadius,
                        ),
                    types = currentState.value.selectedTypes,
                    dateRange =
                        DateRange(
                            from = currentState.value.selectedDateRange.start,
                            to = currentState.value.selectedDateRange.end,
                        ),
                    locale = localeService.currentLocale.value,
                ),
            ),
        )
    }

    private suspend fun updateAroundMeState() {
        when (state.value.aroundMe) {
            SearchCardState.AroundMe.Unselected -> {
                _state.update { it.copy(aroundMe = SearchCardState.AroundMe.Loading) }
                locationService.requestLocation()
            }

            SearchCardState.AroundMe.Selected ->
                _state.update { it.copy(aroundMe = SearchCardState.AroundMe.Unselected) }

            SearchCardState.AroundMe.LocationServicesDisabled ->
                _effects.tryEmit(SearchCardEffect.NotifyLocationServicesDisabled)

            SearchCardState.AroundMe.Loading -> {
                // no- op
            }
        }
    }
}
