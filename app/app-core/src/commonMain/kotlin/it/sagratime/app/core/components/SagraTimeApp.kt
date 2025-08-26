package it.sagratime.app.core.components

import androidx.compose.runtime.Composable
import it.sagratime.app.core.di.DIModules
import it.sagratime.app.core.feature.router.components.SagraTimeNavHost
import org.koin.compose.KoinApplication

@Composable
fun SagraTimeApp() {
    KoinApplication(
        application = {
            modules(
                DIModules.core,
                DIModules.viewModels,
            )
        },
        content = { SagraTimeTheme { SagraTimeNavHost() } },
    )
}
