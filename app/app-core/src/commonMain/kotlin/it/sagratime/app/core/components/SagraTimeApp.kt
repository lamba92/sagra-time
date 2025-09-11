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
fun SagraTimeApp(
    modifier: Modifier = Modifier,
    diModules: List<Module> = DEFAULT_MODULES,
) {
    KoinApplication(
        application = { modules(diModules) },
        content = {
            BoxWithConstraints(
                modifier = modifier.fillMaxSize(),
            ) {
                val screenType =
                    when {
                        maxWidth > 1000.dp -> ScreenType.LARGE
                        maxWidth > 600.dp -> ScreenType.MEDIUM
                        else -> ScreenType.SMALL
                    }
                SagraTimeTheme(screenType) {
                    SagraTimeNavHost()
                }
            }
        },
    )
}
