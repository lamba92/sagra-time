package it.sagratime.app.core.feature.cards.welcome.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.components.SagraTimeTheme
import it.sagratime.app.core.feature.cards.welcome.WelcomeCardState
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.welcome_card_active_events
import it.sagratime.app_core.generated.resources.welcome_card_yearly_event_events
import org.jetbrains.compose.resources.stringResource

@Composable
fun ActiveEventsRow(state: WelcomeCardState) {
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
