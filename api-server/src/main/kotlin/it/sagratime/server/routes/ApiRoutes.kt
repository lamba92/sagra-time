package it.sagratime.server.routes

import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import it.sagratime.server.service.V1ServerEventRepository

fun Routing.apiRoutes(eventProvider: V1ServerEventRepository) {
    route("api") {
        v1Routes(eventProvider)
    }
}
