@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)

package it.sagratime.app.core.feature.cards.search.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SearchCardTitle()
            SearchCardTextField(
                state = state,
                onEvent = onEvent,
            )
            AnimatedVisibility(state.isAdvancedSearch) {
                LocationTextField(state = state, onEvent = onEvent)
            }

            AnimatedVisibility(state.isAdvancedSearch) {
                DistanceSlider(state = state, onEvent = onEvent)
            }

            SearchOptionsRow(
                state = state,
                onEvent = onEvent,
                modifier = modifier,
            )

            AnimatedVisibility(state.isAdvancedSearch) {
                AdvancedSearchCardContent(state = state, onEvent = onEvent)
            }
            PopularSearchesRow(state = state.popularSearches, onEvent = onEvent)

            SearchActionsRow(
                modifier = Modifier.fillMaxWidth(),
                onEvent = onEvent,
            )
        }
    }
}
