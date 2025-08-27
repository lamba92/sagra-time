package it.sagratime.app.core.feature.router.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import it.sagratime.app.core.feature.home.components.SagraTimeHome
import it.sagratime.app.core.feature.router.SagraTimeRoute
import it.sagratime.app.core.feature.router.SagraTimeRouter
import org.koin.compose.koinInject

@Composable
fun SagraTimeNavHost(
    modifier: Modifier = Modifier,
    router: SagraTimeRouter = koinInject(),
) {
    val activeRoute by router.activeRoute.collectAsState()
    SagraTimeNavHost(modifier, activeRoute)
}

@Composable
fun SagraTimeNavHost(
    modifier: Modifier = Modifier.Companion,
    route: SagraTimeRoute,
) {
    when (route) {
        SagraTimeRoute.Home -> SagraTimeHome(modifier)
        is SagraTimeRoute.Sagra -> TODO()
        is SagraTimeRoute.EventType -> TODO()
        is SagraTimeRoute.Region -> TODO()
    }
}
