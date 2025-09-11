package it.sagratime.app.core.feature.cards.welcome.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.welcome_card_background
import org.jetbrains.compose.resources.painterResource

@Composable
fun WelcomeCardBackground(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(Res.drawable.welcome_card_background),
        contentDescription = "welcome card background",
        modifier = modifier,
        contentScale = ContentScale.Crop,
        alignment = Alignment.BottomCenter,
    )
}
