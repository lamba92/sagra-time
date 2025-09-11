package it.sagratime.server.routes

import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import it.sagratime.server.service.EventProvider

fun Routing.apiRoutes(eventProvider: EventProvider) {
    route("api") {
        v1Routes(eventProvider)
    }
}
