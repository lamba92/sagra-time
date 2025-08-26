package it.sagratime.app.core.feature.router.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import it.sagratime.app.core.feature.router.Router
import it.sagratime.app.core.feature.router.SagraTimeRoute
import org.koin.compose.koinInject

@Composable
fun SagraTimeNavHost(
    modifier: Modifier = Modifier.Companion,
    router: Router = koinInject(),
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
        is SagraTimeRoute.EventType -> TODO()
        SagraTimeRoute.Home -> TODO()
        is SagraTimeRoute.Region -> TODO()
        is SagraTimeRoute.Sagra -> TODO()
    }
}
