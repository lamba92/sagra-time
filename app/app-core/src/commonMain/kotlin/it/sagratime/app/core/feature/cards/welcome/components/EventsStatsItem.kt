package it.sagratime.app.core.feature.cards.welcome.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import it.sagratime.app.core.components.ShimmeringBox
import it.sagratime.app.core.feature.cards.welcome.WelcomeCardState

@Composable
fun EventsStatsItem(
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
            WelcomeCardState.Loading ->
                ShimmeringBox(
                    modifier =
                        Modifier.size(
                            100.dp,
                            12.dp,
                        ),
                )

            is WelcomeCardState.Ready -> content(state)
        }
    }
}
