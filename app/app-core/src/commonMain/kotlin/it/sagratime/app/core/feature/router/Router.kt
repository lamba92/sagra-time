package it.sagratime.app.core.feature.router

import kotlinx.coroutines.flow.MutableStateFlow

class Router {
    val activeRoute = MutableStateFlow(SagraTimeRoute.Home)
}
