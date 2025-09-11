package it.sagratime.app.desktop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jthemedetecor.OsThemeDetector
import it.sagratime.app.core.components.IsSystemDarkProvider
import java.util.function.Consumer

class OsDetectorIsSystemDarkProvider :
    IsSystemDarkProvider,
    AutoCloseable {
    private var state by mutableStateOf(OsThemeDetector.getDetector().isDark)
    private val listener = Consumer<Boolean> { state = it }

    init {
        OsThemeDetector.getDetector().registerListener(listener)
    }

    override val isDark: Boolean
        @Composable get() = state

    override fun close() {
        OsThemeDetector.getDetector().removeListener(listener)
    }
}
