package it.sagratime.app.core.feature.cards.search

import it.sagratime.core.data.EventType
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
    value class DateSelectionChanged(
        val dateSelection: SearchCardState.DateSelection,
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
    sealed interface SelectedTypesChanged : SearchCardEvent {
        @Serializable
        @JvmInline
        value class TypeAdded(
            val type: EventType,
        ) : SelectedTypesChanged

        @Serializable
        @JvmInline
        value class TypeRemoved(
            val type: EventType,
        ) : SelectedTypesChanged
    }

    @Serializable
    object SearchClicked : SearchCardEvent

    @Serializable
    object ClearClicked : SearchCardEvent

    @Serializable
    @JvmInline
    value class PopularSearchClick(
        val query: String,
    ) : SearchCardEvent
}
