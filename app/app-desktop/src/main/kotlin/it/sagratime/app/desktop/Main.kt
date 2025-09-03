package it.sagratime.app.desktop

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import it.sagratime.app.core.components.DEFAULT_MODULES
import it.sagratime.app.core.components.LocalCoilComponentsProviders
import it.sagratime.app.core.components.LocalCoilLogger
import it.sagratime.app.core.components.LocalIsSystemDarkProvider
import it.sagratime.app.core.components.SagraTimeApp
import it.sagratime.app.core.di.DIModules


fun main() {
    singleWindowApplication(
        state = WindowState(
            size = DpSize(450.dp, 1000.dp)
        ),
        resizable = true
    ) {
        CompositionLocalProvider(
            LocalCoilLogger provides CoilDebugLogger,
            LocalCoilComponentsProviders provides debugComponentProviders,
            LocalIsSystemDarkProvider provides OsDetectorIsSystemDarkProvider()
        ) {
            SagraTimeApp(
                diModules = DEFAULT_MODULES + DIModules.mocks,
            )
        }
    }
}
