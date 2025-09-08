package it.sagratime.app.core.feature.cards.welcome.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Scale
import it.sagratime.app.core.components.Image
import it.sagratime.app_core.generated.resources.Res

@Composable
fun WelcomeCardBackground(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier,
        model =
            ImageRequest
                .Builder(LocalPlatformContext.current)
                .data(Res.getUri("files/images/welcome-card-background.png"))
                .crossfade(true)
                .scale(Scale.FIT)
                .build(),
        contentDescription = "welcome card background",
        contentScale = ContentScale.Crop,
    )
}
