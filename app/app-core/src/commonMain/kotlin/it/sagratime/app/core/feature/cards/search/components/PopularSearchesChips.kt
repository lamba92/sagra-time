package it.sagratime.app.core.feature.cards.search.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import it.sagratime.app.core.components.PastelChip
import it.sagratime.app.core.feature.cards.search.SearchCardEvent
import it.sagratime.app.core.feature.cards.search.SearchCardState

@Composable
fun PopularSearchesChips(
    state: SearchCardState.PopularSearches,
    onEvent: (SearchCardEvent.PopularSearchClick) -> Unit,
    scrollState: ScrollState,
) {
    Box {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            when (state) {
                SearchCardState.PopularSearches.Loading ->
                    repeat(3) { index ->
                        PastelChip(
                            modifier = Modifier.shimmer(),
                            index = index,
                            label = { Text("margherita", modifier = Modifier.alpha(0f)) },
                        )
                    }

                is SearchCardState.PopularSearches.Loaded ->
                    state.searches.forEachIndexed { index, query ->
                        PastelChip(
                            index = index,
                            onClick = { onEvent(SearchCardEvent.PopularSearchClick(query = query)) },
                            label = { Text(text = query) },
                        )
                    }
            }
        }
    }
}
