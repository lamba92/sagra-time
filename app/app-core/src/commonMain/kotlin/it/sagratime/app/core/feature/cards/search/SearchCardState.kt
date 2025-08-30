@file:OptIn(ExperimentalTime::class)

package it.sagratime.app.core.feature.cards.search

import it.sagratime.app.core.feature.cards.search.components.toLocalDate
import it.sagratime.core.data.EventType
import it.sagratime.core.data.Location
import it.sagratime.core.units.Length
import it.sagratime.core.units.MeasurementSystem
import it.sagratime.core.units.kilometers
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

@Serializable
data class SearchCardState(
    val query: String = "",
    val isAdvancedSearch: Boolean = false,
    val currentLocation: SelectedLocation? = null,
    val selectedDateRange: DateRangeSelection = DateRangeSelection.nextMonth(),
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
    sealed interface SelectedLocation {
        @Serializable
        object AroundMe : SelectedLocation

        @Serializable
        data class Custom(
            val location: Location,
        ) : SelectedLocation
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
    enum class DateButtonSelection {
        Today,
        ThisWeekend,
        NextWeek,
        NextMonth,
        Custom,
    }

    @Serializable
    data class DateRangeSelection(
        val start: LocalDate,
        val end: LocalDate,
    ) {
        companion object {
            fun nextMonth(): DateRangeSelection {
                val now = Clock.System.now()
                val nextMonth = now + 30.days

                return DateRangeSelection(
                    start = now.toLocalDateTime(TimeZone.currentSystemDefault()).toLocalDate(),
                    end = nextMonth.toLocalDateTime(TimeZone.currentSystemDefault()).toLocalDate(),
                )
            }
        }
    }
}
