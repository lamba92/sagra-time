package it.sagratime.app.core.feature.cards.search

import it.sagratime.core.data.EventType
import it.sagratime.core.data.Location
import it.sagratime.core.units.Length
import kotlinx.serialization.Serializable

@Serializable
sealed interface SearchCardEffect {
    @Serializable
    data object NotifyLocationServicesDisabled : SearchCardEffect

    @Serializable
    data class Search(
        val query: String,
        val location: Location?,
        val radius: Length,
        val types: Set<EventType>,
        val dateRange: SearchCardState.DateRangeSelection,
    ) : SearchCardEffect
}
