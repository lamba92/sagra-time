package it.sagratime.app.core.feature.cards.event.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import it.sagratime.app.core.components.CoilImage
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun EventCardBackground(
    backgroundUrl: String?,
    fallback: DrawableResource,
    modifier: Modifier = Modifier,
) {
    when (backgroundUrl) {
        null ->
            Image(
                modifier = modifier,
                painter = painterResource(fallback),
                contentDescription = "event image",
                contentScale = ContentScale.Crop,
            )
        else ->
            CoilImage(
                modifier = modifier,
                model =
                    ImageRequest
                        .Builder(LocalPlatformContext.current)
                        .data(backgroundUrl)
                        .crossfade(true)
                        .build(),
                contentDescription = "event image",
                contentScale = ContentScale.Crop,
            )
    }
}
