package it.sagratime.app.core.feature.cards.welcome.componen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import it.sagratime.app.core.components.Image
import it.sagratime.app.core.components.SagraTimeTheme
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
    modifier: Modifier = Modifier.Companion,
    viewModel: WelcomeCardViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    WelcomeCard(modifier, state)
}

@Composable
fun WelcomeCard(
    modifier: Modifier = Modifier.Companion,
    state: WelcomeCardState,
) {
    SagraTimeTheme(isDark = true) {
        Card(
            modifier =
                modifier
                    .size(400.dp, 280.dp),
            content = {
                WithWhiteLocalContent {
                    Box {
                        Image(
                            model = Res.getUri("files/images/welcome-card-background.png"),
                            contentDescription = "welcome card background",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxHeight()
                                    .background(
                                        brush =
                                            Brush.verticalGradient(
                                                colors =
                                                    listOf(
                                                        Color.Black.copy(alpha = 0.2f),
                                                        Color.Black.copy(alpha = 0.4f),
                                                    ),
                                            ),
                                    ).padding(16.dp),
                            verticalArrangement =
                                Arrangement.spacedBy(
                                    space = 8.dp,
                                    alignment = Alignment.Companion.Bottom,
                                ),
                        ) {
                            Text(
                                text = stringResource(Res.string.welcome_card_title),
                                style = MaterialTheme.typography.headlineMedium,
                            )
                            Text(
                                text = stringResource(Res.string.welcome_card_subtitle),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            ActiveEventsRow(state)
                        }
                    }
                }
            },
        )
    }
}

@Composable
fun WithWhiteLocalContent(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalContentColor provides Color.White,
        content = content,
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
            loadedContent = {
                Text(
                    text =
                        stringResource(
                            Res.string.welcome_card_active_events,
                            it.statistics.activeEvents,
                        ),
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
        )
        EventsStatsItem(
            state = state,
            vector = Icons.Filled.LocationOn,
            loadedContent = {
                Text(
                    text =
                        stringResource(
                            Res.string.welcome_card_yearly_event_events,
                            it.statistics.eventsThisYear,
                        ),
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
        )
    }
}

@Composable
private fun EventsStatsItem(
    state: WelcomeCardState,
    vector: ImageVector,
    loadedContent: @Composable (WelcomeCardState.Ready) -> Unit,
) {
    Row(
        modifier =
            Modifier.Companion.height(20.dp),
        verticalAlignment = Alignment.Companion.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = vector,
            contentDescription = null,
        )
        when (state) {
            WelcomeCardState.Loading -> ShimmeringBox()
            is WelcomeCardState.Ready -> loadedContent(state)
        }
    }
}

@Composable
private fun ShimmeringBox(
    boxSize: DpSize = DpSize(100.dp, 12.dp),
    shimmerColor: Color = LocalContentColor.current,
) {
    Box(
        modifier =
            Modifier.Companion
                .shimmer()
                .size(boxSize),
    ) {
        Box(
            modifier =
                Modifier.Companion
                    .fillMaxSize()
                    .background(shimmerColor),
        )
    }
}
