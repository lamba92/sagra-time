package it.sagratime.app.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.valentinilk.shimmer.shimmer

@Composable
fun ShimmeringBox(
    modifier: Modifier = Modifier,
    shimmerColor: Color = LocalContentColor.current,
) {
    Box(modifier = modifier.shimmer()) {
        Box(
            modifier =
                modifier
                    .fillMaxSize()
                    .background(shimmerColor),
        )
    }
}
