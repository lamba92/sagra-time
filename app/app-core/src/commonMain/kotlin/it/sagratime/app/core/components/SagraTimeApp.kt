package it.sagratime.app.core.components

import androidx.compose.runtime.Composable
import it.sagratime.app.core.di.DIModules
import it.sagratime.app.core.feature.router.components.SagraTimeNavHost
import org.koin.compose.KoinApplication
import org.koin.core.module.Module

val DEFAULT_MODULES =
    listOf(
        DIModules.core,
        DIModules.viewModels,
    )

@Composable
fun SagraTimeApp(diModules: List<Module> = DEFAULT_MODULES) {
    KoinApplication(
        application = { modules(diModules) },
        content = {
            SagraTimeTheme {
                SagraTimeNavHost()
            }
        },
    )
}
