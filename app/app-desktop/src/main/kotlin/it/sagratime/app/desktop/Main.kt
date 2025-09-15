@file:OptIn(DelicateCoroutinesApi::class)

package it.sagratime.app.desktop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import it.sagratime.app.core.components.LocalCoilComponentsProviders
import it.sagratime.app.core.components.LocalCoilLogger
import it.sagratime.app.core.components.LocalIsSystemDarkProvider
import it.sagratime.app.core.components.SagraTimeApp
import it.sagratime.app.core.di.sagraTimeModules
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import org.koin.core.context.startKoin
import org.koin.dsl.koinApplication

fun main() {
    val koinApplication =
        startKoin {
            sagraTimeModules(GlobalScope, true)
        }

    singleWindowApplication(
        state =
            WindowState(
                size = DpSize(450.dp, 1000.dp),
            ),
        resizable = true,
    ) {
        WithDesktopProviders {
            SagraTimeApp(koinApplication)
        }
    }
}

@Composable
private fun WithDesktopProviders(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalCoilLogger provides CoilDebugLogger,
        LocalCoilComponentsProviders provides DebugComponentProviders,
        LocalIsSystemDarkProvider provides OsDetectorIsSystemDarkProvider(),
        content = content,
    )
}
