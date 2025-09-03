package it.sagratime.app.core.feature.cards.search.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.feature.cards.search.SearchCardEvent
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.search_card_clear_search
import it.sagratime.app_core.generated.resources.search_card_search
import org.jetbrains.compose.resources.stringResource

@Composable
fun SearchActionsRow(
    modifier: Modifier = Modifier,
    onEvent: (SearchCardEvent) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedButton(
            onClick = { onEvent(SearchCardEvent.ClearSearchClick) },
            modifier = Modifier.weight(1f),
            content = { Text(stringResource(Res.string.search_card_clear_search)) },
        )
        Button(
            onClick = { onEvent(SearchCardEvent.SearchClicked) },
            modifier = Modifier.weight(1f),
            content = { Text(stringResource(Res.string.search_card_search)) },
        )
    }
}
