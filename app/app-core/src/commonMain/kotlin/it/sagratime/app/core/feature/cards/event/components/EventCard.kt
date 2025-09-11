package it.sagratime.app.core.feature.cards.event.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import it.sagratime.app.core.components.SagraTimeCard
import it.sagratime.core.data.Event

@Composable
fun EventCard(
    event: Event,
    index: Int = 0,
    modifier: Modifier = Modifier,
) {
    SagraTimeCard(modifier = modifier) {
        EventCardHeader(
            event = event,
            index = index,
        )
        EventCardContent(
            event = event,
        )
    }
}
