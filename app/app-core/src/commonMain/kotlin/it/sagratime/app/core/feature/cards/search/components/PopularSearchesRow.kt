package it.sagratime.app.core.feature.cards.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.components.SagraTimeTheme
import it.sagratime.app.core.feature.cards.search.SearchCardEvent
import it.sagratime.app.core.feature.cards.search.SearchCardState
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.popular_searches
import org.jetbrains.compose.resources.stringResource

@Composable
fun PopularSearchesRow(
    modifier: Modifier = Modifier,
    state: SearchCardState.PopularSearches,
    onEvent: (SearchCardEvent.PopularSearchClick) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.popular_searches),
            style = SagraTimeTheme.typography.bodyMedium,
        )

        PopularSearchesChips(
            state = state,
            onEvent = onEvent,
        )
    }
}
