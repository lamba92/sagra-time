package it.sagratime.app.core.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.feature.cards.event.components.EventCard
import it.sagratime.app.core.feature.cards.search.components.SearchCard
import it.sagratime.app.core.feature.cards.welcome.components.WelcomeCard
import it.sagratime.app.core.feature.cards.whatisasagra.components.WhatIsASagraCard
import it.sagratime.app.core.feature.home.HomeEvent
import it.sagratime.app.core.feature.home.HomeState

@Composable
fun HomeContent(
    innerPaddings: PaddingValues,
    modifier: Modifier = Modifier,
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    scrollState: LazyListState,
) {
    HomeContentSmallScreen(modifier, innerPaddings, scrollState, state, onEvent)
}

@Composable
fun HomeContentSmallScreen(
    modifier: Modifier = Modifier,
    innerPaddings: PaddingValues,
    scrollState: LazyListState,
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
) {
    LazyColumn(
        modifier
            .fillMaxWidth()
            .padding(innerPaddings)
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = scrollState,
    ) {
        item {
            Spacer(modifier = Modifier.height(4.dp))
        }

        if (state == HomeState.Default) {
            item {
                WelcomeCard(
                    modifier =
                        Modifier.animateItem(),
                )
            }
        }

        item {
            SearchCard(
                onEffect = { onEvent(HomeEvent.SearchEffect(it)) },
            )
        }
        if (state == HomeState.Default) {
            item {
                WhatIsASagraCard(modifier = Modifier.animateItem())
            }
        }

        when (state) {
            HomeState.Default -> {}
            HomeState.Searching.Loading -> item { CircularProgressIndicator() }
            is HomeState.Searching.Success ->
                items(state.results) { event ->
                    EventCard(event, modifier = Modifier.animateItem())
                }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
