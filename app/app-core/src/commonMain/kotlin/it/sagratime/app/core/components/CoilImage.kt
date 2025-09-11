package it.sagratime.app.core.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.layout.ContentScale
import coil3.ComponentRegistry
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.AsyncImagePainter.Companion.DefaultTransform
import coil3.compose.LocalPlatformContext
import coil3.util.Logger

val LocalCoilLogger: ProvidableCompositionLocal<Logger?> =
    staticCompositionLocalOf { null }

val LocalCoilComponentsProviders: ProvidableCompositionLocal<List<CoilComponentProvider>> =
    staticCompositionLocalOf { emptyList() }

fun interface CoilComponentProvider {
    fun ComponentRegistry.Builder.registerComponent()
}

@Composable
fun CoilImage(
    model: Any?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    transform: (AsyncImagePainter.State) -> AsyncImagePainter.State = DefaultTransform,
    onState: ((AsyncImagePainter.State) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality,
    clipToBounds: Boolean = true,
) {
    val context = LocalPlatformContext.current
    val logger = LocalCoilLogger.current
    val componentsProviders = LocalCoilComponentsProviders.current
    val imageLoader =
        remember(context, logger, componentsProviders) {
            ImageLoader
                .Builder(context)
                .components {
                    componentsProviders.forEach { provider ->
                        with(provider) { registerComponent() }
                    }
                }.logger(logger)
                .build()
        }
    AsyncImage(
        model = model,
        contentDescription = contentDescription,
        imageLoader = imageLoader,
        modifier = modifier,
        transform = transform,
        onState = onState,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
        filterQuality = filterQuality,
        clipToBounds = clipToBounds,
    )
}
