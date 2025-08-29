package it.sagratime.app.desktop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import coil3.util.Logger
import com.jthemedetecor.OsThemeDetector
import it.sagratime.app.core.components.CoilComponentProvider
import it.sagratime.app.core.components.DEFAULT_MODULES
import it.sagratime.app.core.components.IsSystemDarkProvider
import it.sagratime.app.core.components.LocalCoilComponentsProviders
import it.sagratime.app.core.components.LocalCoilLogger
import it.sagratime.app.core.components.LocalIsSystemDarkProvider
import it.sagratime.app.core.components.SagraTimeApp
import it.sagratime.app.core.di.DIModules


fun main() {
    val isSystemDarkProvider = getSystemDarkProvider()
    singleWindowApplication(
        state = WindowState(
            size = DpSize(450.dp, 900.dp)
        ),
        resizable = false
    ) {
        CompositionLocalProvider(
            LocalCoilLogger provides CoilDebugLogger,
            LocalCoilComponentsProviders provides debugComponentProviders,
            LocalIsSystemDarkProvider provides isSystemDarkProvider
        ) {
            SagraTimeApp(
                diModules = DEFAULT_MODULES + DIModules.mocks,
            )
        }
    }
}

private fun getSystemDarkProvider(): IsSystemDarkProvider {
    val detector = OsThemeDetector.getDetector()
    val isDark = mutableStateOf(detector.isDark)
    detector.registerListener { isDark.value = it }
    val isSystemDarkProvider = object : IsSystemDarkProvider {
        override val isDark: Boolean
            @Composable get() = isDark.value

    }
    return isSystemDarkProvider
}

val debugComponentProviders = listOf(CoilComponentProvider {
    add(JarUriFetcherFactory)
})

object CoilDebugLogger : Logger {

    override var minLevel = Logger.Level.Debug

    override fun log(
        tag: String,
        level: Logger.Level,
        message: String?,
        throwable: Throwable?,
    ) {
        when (level) {
            Logger.Level.Debug -> println("[DEBUG] $tag - $message")
            Logger.Level.Error -> println("[ERROR] $tag - $message")
            Logger.Level.Info -> println("[INFO] $tag - $message")
            Logger.Level.Warn -> println("[WARN] $tag - $message")
            Logger.Level.Verbose -> println("[VERBOSE] $tag - $message")
        }
    }
}