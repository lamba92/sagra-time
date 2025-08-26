package it.sagratime.app.core.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScaffold(
    title: String = stringResource(Res.string.app_name),
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
}
