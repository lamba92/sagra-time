package it.sagratime.app.core.feature.cards.search.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.components.SagraTimeTheme
import it.sagratime.app.core.components.SagraTimeToggleButton
import it.sagratime.app.core.components.disabledColors
import it.sagratime.app.core.feature.cards.search.SearchCardState

@Composable
fun AroundMeButton(
    modifier: Modifier = Modifier,
    state: SearchCardState.AroundMe,
    onClick: () -> Unit,
) {
    when (state) {
        SearchCardState.AroundMe.Selected ->
            SagraTimeToggleButton(
                onClick = onClick,
                isActive = true,
                modifier = modifier,
                shape = SagraTimeTheme.shapes.medium,
                content = { AroundMeButtonContent(Icons.Filled.LocationOn) },
            )

        SearchCardState.AroundMe.Loading ->
            SagraTimeToggleButton(
                onClick = onClick,
                isActive = true,
                enabled = false,
                modifier = modifier,
                shape = SagraTimeTheme.shapes.medium,
                content = { CircularProgressIndicator(modifier = Modifier.size(16.dp)) },
            )

        SearchCardState.AroundMe.Unselected ->
            SagraTimeToggleButton(
                onClick = onClick,
                isActive = false,
                modifier = modifier,
                shape = SagraTimeTheme.shapes.medium,
                content = { AroundMeButtonContent(Icons.Filled.LocationOn) },
            )

        SearchCardState.AroundMe.LocationServicesDisabled ->
            SagraTimeToggleButton(
                onClick = onClick,
                isActive = false,
                colors = ButtonDefaults.disabledColors(),
                modifier = modifier,
                shape = SagraTimeTheme.shapes.medium,
                content = { AroundMeButtonContent(Icons.Filled.LocationOff) },
            )
    }
}
