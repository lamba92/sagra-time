package it.sagratime.app.core.feature.cards.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.feature.cards.search.SearchCardEvent
import it.sagratime.app.core.feature.cards.search.SearchCardState

@Composable
fun SimpleSearchCardContent(
    state: SearchCardState,
    modifier: Modifier = Modifier,
    onEvent: (SearchCardEvent) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AroundMeButton(
                state = state.aroundMe,
                onClick = { onEvent(SearchCardEvent.AroundMeClicked) },
                modifier = modifier.weight(0.5f),
            )
            AdvancedFiltersButton(
                onClick = { onEvent(SearchCardEvent.AdvancedSearchClicked) },
                modifier = modifier.weight(0.5f),
            )
        }
        PopularSearchesRow(state = state.popularSearches, onEvent = onEvent)
    }
}
