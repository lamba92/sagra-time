package it.sagratime.app.core.feature.cards.event.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Scale
import it.sagratime.app.core.components.Image
import it.sagratime.app.core.components.PastelChip
import it.sagratime.app.core.components.SagraTimeTheme
import it.sagratime.app.core.components.scrimBrush
import it.sagratime.core.data.Event

@Composable
fun EventCardHeader(
    event: Event,
    index: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .height(250.dp)
                .fillMaxWidth(),
    ) {
        Image(
            modifier = Modifier.matchParentSize(),
            model =
                ImageRequest
                    .Builder(LocalPlatformContext.current)
                    .data(event.imageUrl ?: event.type.placeholderImage())
                    .crossfade(true)
                    .scale(Scale.FIT)
                    .build(),
            contentDescription = "event image",
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier =
                modifier
                    .background(brush = scrimBrush())
                    .padding(SagraTimeTheme.metrics.cards.innerPaddings)
                    .height(250.dp)
                    .fillMaxWidth(),
        ) {
            Box(
                modifier =
                    modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth(),
            ) {
                PastelChip(
                    modifier = modifier.align(Alignment.CenterStart),
                    index = index,
                    vividness = 2f,
                    label = {
                        Text(
                            text = event.localizedDateString(),
                            style = SagraTimeTheme.typography.labelLarge,
                            color = Color.White,
                        )
                    },
                )
                PastelChip(
                    modifier = modifier.align(Alignment.CenterEnd),
                    index = index,
                    vividness = 1f,
                    label = {
                        Text(
                            text = event.localizedDays(),
                            style = SagraTimeTheme.typography.labelLarge,
                            color = Color.White,
                        )
                    },
                )
            }
            Column(
                modifier = modifier.align(Alignment.BottomStart),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = event.name,
                    style = SagraTimeTheme.typography.titleLarge,
                    color = Color.White,
                )
                Row {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = "location icon",
                        tint = Color.White,
                    )
                    Text(
                        text = event.location.cityName,
                        style = SagraTimeTheme.typography.labelLarge,
                        color = Color.White,
                    )
                }
            }
        }
    }
}
