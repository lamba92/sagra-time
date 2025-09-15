@file:OptIn(ExperimentalComposeUiApi::class, DelicateCoroutinesApi::class)

package it.sagratime.app.browser

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeViewport
import it.sagratime.app.core.components.SagraTimeApp
import it.sagratime.app.core.di.sagraTimeModules
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import org.koin.core.context.startKoin

fun main() {

    val koinApplication = startKoin {
        sagraTimeModules(GlobalScope, true)
    }

    ComposeViewport {
        SagraTimeApp(
            koinApplication = koinApplication,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
