@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)

package it.sagratime.app.core.feature.cards.search.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.components.SagraTimeCard
import it.sagratime.app.core.components.SagraTimeTheme
import it.sagratime.app.core.feature.cards.search.SearchCardEffect
import it.sagratime.app.core.feature.cards.search.SearchCardEvent
import it.sagratime.app.core.feature.cards.search.SearchCardState
import it.sagratime.app.core.feature.cards.search.SearchCardViewModel
import org.koin.compose.koinInject
import kotlin.time.ExperimentalTime

@Composable
fun SearchCard(
    modifier: Modifier = Modifier,
    viewModel: SearchCardViewModel = koinInject(),
    onEffect: (SearchCardEffect) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            onEffect(effect)
        }
    }
    SearchCard(
        modifier = modifier,
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun SearchCard(
    modifier: Modifier = Modifier,
    state: SearchCardState,
    onEvent: (SearchCardEvent) -> Unit,
) {
    SagraTimeCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(SagraTimeTheme.metrics.cards.innerPaddings),
        ) {
            SearchCardTitle()
            Spacer(modifier = Modifier.height(12.dp))
            SearchCardTextField(
                state = state,
                onEvent = onEvent,
            )
            Spacer(modifier = Modifier.height(12.dp))
            AnimatedContent(targetState = state.isAdvancedSearch) { isAdvancedSearch ->
                if (isAdvancedSearch) {
                    Column {
                        LocationTextField(state, onEvent)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

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

            PopularSearchesRow(state = state.popularSearches, onEvent = onEvent)
            AnimatedContent(targetState = state.isAdvancedSearch) { isAdvancedSearch ->
                Spacer(modifier = Modifier.height(12.dp))
                if (isAdvancedSearch) {
                    AdvancedSearchCardContent(state = state, onEvent = onEvent)
                }
            }

            SearchActionsRow(
                modifier = Modifier.fillMaxWidth(),
                onEvent = onEvent,
            )
        }
    }
}
