@file:OptIn(ExperimentalMaterial3Api::class)

package it.sagratime.app.core.feature.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FoodBank
import androidx.compose.material.icons.outlined.MoveUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.components.RoundedIcon
import it.sagratime.app.core.feature.home.HomeEffect
import it.sagratime.app.core.feature.home.HomeEvent
import it.sagratime.app.core.feature.home.HomeState
import it.sagratime.app.core.feature.home.HomeViewModel
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.app_name
import it.sagratime.app_core.generated.resources.location_services_disabled
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SagraTimeHome(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val locationServicesDisabledString = stringResource(Res.string.location_services_disabled)

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                HomeEffect.NotifyLocationServicesDisabled ->
                    snackbarHostState.showSnackbar(
                        message = locationServicesDisabledString,
                        withDismissAction = true,
                    )
            }
        }
    }

    SagraTimeHome(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        state = state,
        onEvent = viewModel::onEvent,
    )
}

@Composable
fun SagraTimeHome(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    scrollState: LazyListState = rememberLazyListState(),
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                modifier = Modifier.clickable { onEvent(HomeEvent.TopBarTitleClick) },
                title = { Text(stringResource(Res.string.app_name)) },
                navigationIcon = {
                    RoundedIcon(
                        modifier = Modifier.padding(horizontal = 8.dp),
                    ) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            imageVector = Icons.Outlined.FoodBank,
                            contentDescription = null,
                            tint = Color.White,
                        )
                    }
                },
            )
        },
        content = { innerPaddings ->
            HomeContent(
                innerPaddings = innerPaddings,
                state = state,
                onEvent = onEvent,
                scrollState = scrollState,
            )
        },
        floatingActionButton = {
            val isTopReached =
                scrollState.firstVisibleItemIndex == 0 &&
                    scrollState.firstVisibleItemScrollOffset == 0
            val isScrollToTopButtonVisible =
                state is HomeState.Searching && !isTopReached

            AnimatedVisibility(
                visible = isScrollToTopButtonVisible,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                val scope = rememberCoroutineScope()
                FloatingActionButton(
                    onClick = { scope.launch { scrollState.animateScrollToItem(0) } },
                    content = {
                        Icon(
                            imageVector = Icons.Outlined.MoveUp,
                            contentDescription = null,
                        )
                    },
                )
            }
        },
    )
}
