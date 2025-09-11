package it.sagratime.app.core.feature.cards.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.feature.cards.search.SearchCardEvent
import it.sagratime.app.core.feature.cards.search.SearchCardState

@Composable
fun SearchOptionsRow(
    state: SearchCardState,
    onEvent: (SearchCardEvent) -> Unit,
    modifier: Modifier,
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
        AdvancedFiltersToggleButton(
            isActive = state.isAdvancedSearch,
            onClick = { onEvent(SearchCardEvent.AdvancedSearchClicked) },
            modifier = modifier.weight(0.5f),
        )
    }
}
