package it.sagratime.app.core.feature.home.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.components.SagraTimeTheme
import it.sagratime.app.core.feature.cards.search.SearchCardEffect
import it.sagratime.app.core.feature.cards.search.components.SearchCard
import it.sagratime.app.core.feature.cards.welcome.components.WelcomeCard
import it.sagratime.app.core.feature.cards.whatisasagra.components.WhatIsASagraCard
import kotlinx.coroutines.launch

@Composable
fun HomeContent(
    innerPaddings: PaddingValues,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier.Companion,
    scrollState: ScrollState = rememberScrollState(),
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.FixedSize(SagraTimeTheme.metrics.cards.width),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier =
            modifier
                .fillMaxWidth()
                .padding(innerPaddings)
                .padding(horizontal = 8.dp),
        content = {
            item {
                WelcomeCard()
            }
            item {
                val scope = rememberCoroutineScope()
                SearchCard {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            when (it) {
                                SearchCardEffect.NotifyLocationServicesDisabled ->
                                    "Location services are disabled!"

                                is SearchCardEffect.Search ->
                                    "Search not yet implemented!\nQuery: $it"
                            },
                            withDismissAction = true,
                        )
                    }
                }
            }
            item {
                WhatIsASagraCard()
            }
        },
    )
}
