package it.sagratime.app.core.feature.cards.event.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.components.PastelChip
import it.sagratime.app.core.components.SagraTimeTheme
import it.sagratime.core.data.Event

@Composable
fun EventCardContent(
    event: Event,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .padding(SagraTimeTheme.metrics.cards.innerPaddings)
                .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        event.description?.let {
            Text(
                text = it,
                style = SagraTimeTheme.typography.bodyLarge,
                color = SagraTimeTheme.colorScheme.onSurfaceVariant,
            )
        }
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            event.food.forEachIndexed { index, food ->
                PastelChip(
                    index = index,
                    label = {
                        Text(
                            text = food,
                            style = SagraTimeTheme.typography.labelLarge,
                        )
                    },
                )
            }
        }
    }
}
