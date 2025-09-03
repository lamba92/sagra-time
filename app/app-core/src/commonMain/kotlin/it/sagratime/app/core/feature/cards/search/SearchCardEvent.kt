package it.sagratime.app.core.feature.cards.search

import it.sagratime.core.data.EventType
import it.sagratime.core.data.Location
import it.sagratime.core.units.Length
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
sealed interface SearchCardEvent {
    @Serializable
    @JvmInline
    value class QueryChanged(
        val query: String,
    ) : SearchCardEvent

    @Serializable
    @JvmInline
    value class DateRangeSelectionChanged(
        val selection: SearchCardState.DateRangeSelection,
    ) : SearchCardEvent

    @Serializable
    @JvmInline
    value class SearchRadiusChanged(
        val searchRadius: Length,
    ) : SearchCardEvent

    @Serializable
    object AdvancedSearchClicked : SearchCardEvent

    @Serializable
    object AdvancedSearchDismissed : SearchCardEvent

    @Serializable
    object AroundMeClicked : SearchCardEvent

    @Serializable
    @JvmInline
    value class SelectedTypesChanged(
        val type: EventType,
    ) : SearchCardEvent

    @Serializable
    object SearchClicked : SearchCardEvent

    @Serializable
    object ClearClicked : SearchCardEvent

    @Serializable
    @JvmInline
    value class PopularSearchClick(
        val query: String,
    ) : SearchCardEvent

    @Serializable
    @JvmInline
    value class LocationQueryChanged(
        val query: String,
    ) : SearchCardEvent

    @Serializable
    @JvmInline
    value class LocationTipCLick(
        val location: Location,
    ) : SearchCardEvent

    @Serializable
    object ClearLocationSearchClick : SearchCardEvent
}
