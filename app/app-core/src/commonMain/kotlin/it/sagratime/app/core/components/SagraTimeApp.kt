package it.sagratime.app.core.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize(),
            ) {
                val screenType = when {
                    maxWidth > 1200.dp -> ScreenType.LARGE
                    maxWidth > 800.dp -> ScreenType.MEDIUM
                    else -> ScreenType.SMALL
                }
                SagraTimeTheme(screenType) {
                    SagraTimeNavHost()
                }
            }
        },
    )
}
