package it.sagratime.app.core.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.rememberDynamicColorScheme

@Composable
fun SagraTimeTheme(
    seedColor: Color = Color.Companion.White,
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme =
        rememberDynamicColorScheme(
            seedColor = seedColor,
            isDark = isDark,
        )
    MaterialTheme(colorScheme, content = content)
}
