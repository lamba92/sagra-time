@file:OptIn(ExperimentalComposeUiApi::class)

package it.sagratime.app.browser

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeViewport
import it.sagratime.app.core.components.DEFAULT_MODULES
import it.sagratime.app.core.components.SagraTimeApp
import it.sagratime.app.core.di.DIModules

fun main() {
    ComposeViewport {
        SagraTimeApp(
            modifier = Modifier.fillMaxSize(),
            diModules = DEFAULT_MODULES + DIModules.mocks,
        )
    }
}
