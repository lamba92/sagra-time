@file:OptIn(ExperimentalMaterial3Api::class)

package it.sagratime.app.core.feature.home.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.components.RoundedIcon
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.app_name
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
            HomeContent(innerPaddings, snackbarHostState)
        },
    )
}
