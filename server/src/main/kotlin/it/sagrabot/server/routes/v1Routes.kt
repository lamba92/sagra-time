package it.sagrabot.server.routes

import com.github.lamba92.kotlin.document.store.core.ObjectCollection
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import it.sagrabot.server.Principal
import it.sagrabot.core.data.Sagra
import kotlin.math.pow
import kotlin.math.sqrt
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.ktor.ext.get

fun Route.v1Routes(sagreCollection: Deferred<ObjectCollection<Sagra>>) {
    route("v1") {
        route("sagra") {
            get("all") {

                val page = call.parameters["page"]?.toIntOrNull() ?: 0
                val size = call.parameters["size"]?.toIntOrNull() ?: 25

                val sagras = sagreCollection
                    .await()
                    .iterateAll()
                    .toList()
                    .drop(page * size)
                    .take(size)

                call.respond(sagras)
            }
            get("search") {
                val query = call.parameters["q"] ?: call.parameters["query"] ?: ""
                val page = call.parameters["page"]?.toIntOrNull() ?: 0
                val size = call.parameters["size"]?.toIntOrNull() ?: 25
                val from = call.parameters["from"]?.let { LocalDateTime.parse(it) }
                    ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

                val sagre = sagreCollection
                    .await()
                    .iterateAll()
                    .filter {
                        it.description?.contains(query, true) == true
                                || it.food.any { it.contains(query, true) }
                                || it.name.contains(query, true)
                                || it.location.region.name.contains(query, true)
                                || it.location.cityName.contains(query, true)
                    }
                    .filter { it.from > from }
                    .drop(page * size)
                    .take(size)
                    .toList()

                call.respond(sagre)
            }
            get("geosearch") {
                val lat = call.parameters["lat"]?.toDoubleOrNull() ?: call.parameters["latitude"]?.toDoubleOrNull()
                val lng = call.parameters["lng"]?.toDoubleOrNull() ?: call.parameters["longitude"]?.toDoubleOrNull()
                val radius = call.parameters["radius"]?.toDoubleOrNull() ?: 1.0
                val page = call.parameters["page"]?.toIntOrNull() ?: 0
                val size = call.parameters["size"]?.toIntOrNull() ?: 25
                val from = call.parameters["from"]?.let { LocalDateTime.parse(it) }
                    ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

                if (lat == null || lng == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val sagre = sagreCollection
                    .await()
                    .iterateAll()
                    .filter { sqrt((lat - it.location.lat).pow(2) + (lng - it.location.lon).pow(2)) < radius }
                    .filter { it.from > from }
                    .drop(page * size)
                    .take(size)
                    .toList()

                call.respond(sagre)
            }
            authenticate {
                post("add") {
                    if (call.principal<Principal>() != Principal.Admin)
                        return@post call.respond(HttpStatusCode.Unauthorized)
                    val sagra = call.receive<Sagra>()
                    sagreCollection
                        .await()
                        .insert(sagra)
                    call.respond(HttpStatusCode.OK)
                }
            }
        }
    }
}
