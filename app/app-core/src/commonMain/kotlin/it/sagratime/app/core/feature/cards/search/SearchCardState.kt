package it.sagratime.app.core.feature.cards.search

import it.sagratime.core.data.EventType
import it.sagratime.core.units.Length
import it.sagratime.core.units.MeasurementSystem
import it.sagratime.core.units.kilometers
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class SearchCardState(
    val query: String = "",
    val isAdvancedSearch: Boolean = false,
    val dateSelection: DateSelection = DateSelection.Today,
    val searchRadius: Length = 50.kilometers,
    val measurementSystem: MeasurementSystem = MeasurementSystem.Metric,
    val selectedTypes: Set<EventType> = emptySet(),
    val aroundMe: AroundMe = AroundMe.Unselected,
    val popularSearches: PopularSearches = PopularSearches.Loading,
) {
    companion object {
        val DEFAULT = SearchCardState()
    }

    @Serializable
    sealed interface PopularSearches {
        @Serializable
        data object Loading : PopularSearches

        @Serializable
        data class Loaded(
            val searches: List<String>,
        ) : PopularSearches
    }

    @Serializable
    enum class AroundMe {
        Unselected,
        Loading,
        Selected,
        LocationServicesDisabled,
    }

    @Serializable
    sealed interface DateSelection {
        @Serializable
        object Today : DateSelection

        @Serializable
        object ThisWeekend : DateSelection

        @Serializable
        object NextWeek : DateSelection

        @Serializable
        data class Custom(
            val from: LocalDate,
            val to: LocalDate?,
        ) : DateSelection
    }
}
