package it.sagratime.app.core.feature.router

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SagraTimeRouter {
    private val _activeRoute: MutableStateFlow<SagraTimeRoute> =
        MutableStateFlow(SagraTimeRoute.Home)

    val activeRoute = _activeRoute.asStateFlow()

    private val backstack = mutableListOf<SagraTimeRoute>()
    private val forwardstack = mutableListOf<SagraTimeRoute>()

    fun navigateTo(route: SagraTimeRoute) {
        backstack.add(_activeRoute.value)
        forwardstack.clear()
        _activeRoute.value = route
    }

    fun back() {
        val previousRoute = backstack.removeLast()
        forwardstack.add(_activeRoute.value)
        _activeRoute.value = previousRoute
    }

    fun forward() {
        val nextRoute = forwardstack.removeLast()
        backstack.add(_activeRoute.value)
        _activeRoute.value = nextRoute
    }
}
