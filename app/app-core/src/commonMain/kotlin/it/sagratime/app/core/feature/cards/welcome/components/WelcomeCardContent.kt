package it.sagratime.app.core.feature.cards.welcome.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.components.SagraTimeTheme
import it.sagratime.app.core.components.scrimBrush
import it.sagratime.app.core.feature.cards.welcome.WelcomeCardState
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.welcome_card_subtitle
import it.sagratime.app_core.generated.resources.welcome_card_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun WelcomeCardContent(state: WelcomeCardState) {
    Column(
        modifier =
            Modifier
                .background(brush = scrimBrush())
                .padding(SagraTimeTheme.metrics.cards.innerPaddings),
        verticalArrangement =
            Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.Bottom,
            ),
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        Text(
            text = stringResource(Res.string.welcome_card_title),
            style = SagraTimeTheme.typography.headlineMedium,
        )
        Text(
            text = stringResource(Res.string.welcome_card_subtitle),
            style = SagraTimeTheme.typography.bodyLarge,
        )
        ActiveEventsRow(state)
    }
}
