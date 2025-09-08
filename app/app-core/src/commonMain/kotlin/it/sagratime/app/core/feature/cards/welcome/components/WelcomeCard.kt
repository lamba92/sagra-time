package it.sagratime.app.core.feature.cards.welcome.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import it.sagratime.app.core.components.SagraTimeCard
import it.sagratime.app.core.components.WithLocalContent
import it.sagratime.app.core.feature.cards.welcome.WelcomeCardState
import it.sagratime.app.core.feature.cards.welcome.WelcomeCardViewModel
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
