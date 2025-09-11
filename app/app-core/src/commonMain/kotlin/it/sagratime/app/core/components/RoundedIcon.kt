package it.sagratime.app.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RoundedIcon(
    backgroundColor: Color = Color.Red,
    size: Dp = 48.dp,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
) {
    Box(
        modifier =
            modifier
                .size(size)
                .background(backgroundColor, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        icon()
    }
}
