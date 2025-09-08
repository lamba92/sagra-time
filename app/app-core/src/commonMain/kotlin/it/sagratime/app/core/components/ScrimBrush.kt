package it.sagratime.app.core.components

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun scrimBrush(): Brush =
    Brush.Companion.verticalGradient(
        colors =
            listOf(
                Color.Companion.Black.copy(alpha = 0.2f),
                Color.Companion.Black.copy(alpha = 0.4f),
            ),
    )
