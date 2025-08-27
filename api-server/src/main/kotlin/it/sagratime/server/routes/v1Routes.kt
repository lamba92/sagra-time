@file:OptIn(ExperimentalTime::class)

package it.sagratime.server.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import it.sagratime.core.data.Event
import it.sagratime.core.data.GeoCoordinates
import it.sagratime.server.Principal
import it.sagratime.server.service.LocationQuery
import it.sagratime.server.service.SagraProvider
import kotlinx.datetime.LocalDateTime
import kotlin.time.ExperimentalTime

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
                        from = GeoCoordinates(lat, lng),
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
                    val event = call.receive<Event>()
                    sagraProvider.addSagra(event)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}
