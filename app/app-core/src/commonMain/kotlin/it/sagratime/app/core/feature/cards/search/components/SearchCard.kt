@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)

package it.sagratime.app.core.feature.cards.search.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import it.sagratime.app.core.components.ScreenType
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
        when (SagraTimeTheme.metrics.screenType) {
            ScreenType.SMALL -> SearchCardSmallScreenContent(state, onEvent, modifier)
            ScreenType.MEDIUM -> SearchCardMediumScreenContent(state, onEvent, modifier)
            ScreenType.LARGE -> SearchCardLargeScreenContent(state, onEvent, modifier)
        }
    }
}

@Composable
fun SearchCardLargeScreenContent(
    state: SearchCardState,
    onEvent: (SearchCardEvent) -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = Modifier.padding(SagraTimeTheme.metrics.cards.innerPaddings),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SearchCardTitle()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Column(
                modifier = Modifier.weight(0.4f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                SearchCardTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = state,
                    onEvent = onEvent,
                )
                LocationTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = state,
                    onEvent = onEvent,
                )
                DistanceSlider(state = state, onEvent = onEvent)

                SearchOptionsRow(
                    state = state,
                    onEvent = onEvent,
                    modifier = modifier,
                )
                PopularSearchesRow(
                    state = state.popularSearches,
                    onEvent = onEvent,
                )
                LargeScreenTypeSelectionGroup(
                    modifier = Modifier.fillMaxWidth(),
                    state = state,
                    onEvent = onEvent,
                )
            }
            DateSelectionGroup(
                modifier = Modifier.weight(0.2f),
                state = state,
                onEvent = onEvent,
            )
        }

        SearchActionsRow(
            modifier = Modifier.width(300.dp).align(Alignment.End),
            onEvent = onEvent,
        )
    }
}

@Composable
fun SearchCardMediumScreenContent(
    state: SearchCardState,
    onEvent: (SearchCardEvent) -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = Modifier.padding(SagraTimeTheme.metrics.cards.innerPaddings),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SearchCardTitle()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SearchCardTextField(
                modifier = Modifier.weight(1f),
                state = state,
                onEvent = onEvent,
            )
            LocationTextField(
                modifier = Modifier.weight(1f),
                state = state,
                onEvent = onEvent,
            )
        }

        AnimatedVisibility(state.isAdvancedSearch) {
            DistanceSlider(state = state, onEvent = onEvent)
        }

        SearchOptionsRow(
            state = state,
            onEvent = onEvent,
            modifier = modifier,
        )

        PopularSearchesRow(
            state = state.popularSearches,
            onEvent = onEvent,
        )

        AnimatedVisibility(state.isAdvancedSearch) {
            AdvancedSearchCardContent(state = state, onEvent = onEvent)
        }

        SearchActionsRow(
            modifier =
                Modifier
                    .fillMaxWidth(0.7f)
                    .align(Alignment.CenterHorizontally),
            onEvent = onEvent,
        )
    }
}

@Composable
fun SearchCardSmallScreenContent(
    state: SearchCardState,
    onEvent: (SearchCardEvent) -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = Modifier.padding(SagraTimeTheme.metrics.cards.innerPaddings),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SearchCardTitle()
        SearchCardTextField(
            modifier = Modifier.fillMaxWidth(),
            state = state,
            onEvent = onEvent,
        )
        AnimatedVisibility(state.isAdvancedSearch) {
            LocationTextField(
                modifier = Modifier.fillMaxWidth(),
                state = state,
                onEvent = onEvent,
            )
        }

        AnimatedVisibility(state.isAdvancedSearch) {
            DistanceSlider(state = state, onEvent = onEvent)
        }

        SearchOptionsRow(
            state = state,
            onEvent = onEvent,
            modifier = modifier,
        )

        PopularSearchesRow(
            state = state.popularSearches,
            onEvent = onEvent,
        )

        AnimatedVisibility(state.isAdvancedSearch) {
            AdvancedSearchCardContent(state = state, onEvent = onEvent)
        }

        SearchActionsRow(
            modifier = Modifier.fillMaxWidth(),
            onEvent = onEvent,
        )
    }
}
