@file:OptIn(ExperimentalTime::class)

package it.sagrabot.server.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import it.sagrabot.core.data.Coordinates
import it.sagrabot.core.data.Sagra
import it.sagrabot.server.Principal
import it.sagrabot.server.service.LocationQuery
import it.sagrabot.server.service.SagraProvider
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDateTime

fun Route.v1Routes(sagraProvider: SagraProvider) {
    route("v1") {
        route("sagra") {
            get("search") {

                val query = call.parameters["q"] ?: call.parameters["query"]
                val page = call.parameters["page"]?.toIntOrNull() ?: 0
                val size = call.parameters["size"]?.toIntOrNull() ?: 25
                val from = call.parameters["from"]?.let { LocalDateTime.parse(it) }
                val radius = call.parameters["radius"]?.toIntOrNull() ?: 10
                val lat = call.parameters["lat"]?.toDoubleOrNull() ?: call.parameters["latitude"]?.toDoubleOrNull()
                val lng = call.parameters["lng"]?.toDoubleOrNull() ?: call.parameters["longitude"]?.toDoubleOrNull()

                val locationQuery = when {
                    lat != null && lng != null -> LocationQuery(
                        from = Coordinates(lat, lng),
                        radiusInKm = radius
                    )

                    else -> null
                }

                val message = sagraProvider.search(
                    page = page,
                    size = size,
                    searchQuery = query,
                    locationQuery = locationQuery,
                    from = from,
                )

                call.respond(message)
            }

            authenticate {
                post("add") {
                    if (call.principal<Principal>() != Principal.Admin)
                        return@post call.respond(HttpStatusCode.Unauthorized)
                    val sagra = call.receive<Sagra>()
                    sagraProvider.addSagra(sagra)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}
