package it.sagratime.app.core.feature.cards.welcome.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.components.Image
import it.sagratime.app.core.components.SagraTimeCard
import it.sagratime.app.core.components.SagraTimeTheme
import it.sagratime.app.core.components.ShimmeringBox
import it.sagratime.app.core.components.WithLocalContent
import it.sagratime.app.core.feature.cards.welcome.WelcomeCardState
import it.sagratime.app.core.feature.cards.welcome.WelcomeCardViewModel
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.welcome_card_active_events
import it.sagratime.app_core.generated.resources.welcome_card_subtitle
import it.sagratime.app_core.generated.resources.welcome_card_title
import it.sagratime.app_core.generated.resources.welcome_card_yearly_event_events
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WelcomeCard(
    modifier: Modifier = Modifier,
    viewModel: WelcomeCardViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    WelcomeCard(modifier, state)
}

@Composable
fun WelcomeCard(
    modifier: Modifier = Modifier,
    state: WelcomeCardState,
) {
    SagraTimeCard(modifier = modifier) {
        WithLocalContent(Color.White) {
            Box {
                WelcomeCardBackground(Modifier.matchParentSize())
                WelcomeCardContent(state)
            }
        }
    }
}

@Composable
private fun WelcomeCardContent(state: WelcomeCardState) {
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

private fun scrimBrush(): Brush =
    Brush.verticalGradient(
        colors =
            listOf(
                Color.Black.copy(alpha = 0.2f),
                Color.Black.copy(alpha = 0.4f),
            ),
    )

@Composable
private fun WelcomeCardBackground(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        model = Res.getUri("files/images/welcome-card-background.png"),
        contentDescription = "welcome card background",
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun ActiveEventsRow(state: WelcomeCardState) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        EventsStatsItem(
            state = state,
            vector = Icons.Filled.CalendarToday,
            content = {
                Text(
                    text =
                        stringResource(
                            Res.string.welcome_card_active_events,
                            it.statistics.activeEvents,
                        ),
                    style = SagraTimeTheme.typography.bodyMedium,
                )
            },
        )
        EventsStatsItem(
            state = state,
            vector = Icons.Filled.LocationOn,
            content = {
                Text(
                    text =
                        stringResource(
                            Res.string.welcome_card_yearly_event_events,
                            it.statistics.eventsThisYear,
                        ),
                    style = SagraTimeTheme.typography.bodyMedium,
                )
            },
        )
    }
}

@Composable
private fun EventsStatsItem(
    state: WelcomeCardState,
    vector: ImageVector,
    content: @Composable (WelcomeCardState.Ready) -> Unit,
) {
    Row(
        modifier = Modifier.height(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = vector,
            contentDescription = null,
        )
        when (state) {
            WelcomeCardState.Loading -> ShimmeringBox(modifier = Modifier.size(100.dp, 12.dp))
            is WelcomeCardState.Ready -> content(state)
        }
    }
}
