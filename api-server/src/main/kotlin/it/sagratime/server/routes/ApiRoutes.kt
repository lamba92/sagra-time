package it.sagratime.server.routes

import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import it.sagratime.server.service.SagraProvider

fun Routing.apiRoutes(sagraProvider: SagraProvider) {
    route("api") {
        v1Routes(sagraProvider)
    }
}