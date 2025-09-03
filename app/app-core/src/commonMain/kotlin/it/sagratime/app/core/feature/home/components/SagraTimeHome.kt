@file:OptIn(ExperimentalMaterial3Api::class)

package it.sagratime.app.core.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FoodBank
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.feature.cards.search.SearchCardEffect
import it.sagratime.app.core.feature.cards.search.components.SearchCard
import it.sagratime.app.core.feature.cards.welcome.components.WelcomeCard
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.app_name
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun SagraTimeHome(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.app_name)) },
                navigationIcon = {
                    Box(
                        modifier =
                            Modifier
                                .padding(horizontal = 8.dp)
                                .size(48.dp)
                                .background(Color.Red, shape = CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            modifier =
                                Modifier
                                    .size(36.dp),
                            imageVector = Icons.Outlined.FoodBank,
                            contentDescription = null,
                            tint = Color.White,
                        )
                    }
                },
            )
        },
        content = { innerPaddings ->
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(innerPaddings)
                        .padding()
                        .padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                WelcomeCard()
                Spacer(modifier = Modifier.height(8.dp))
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
                Spacer(modifier = Modifier.height(8.dp))
            }
        },
    )
}
