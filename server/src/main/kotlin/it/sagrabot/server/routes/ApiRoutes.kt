package it.sagrabot.server.routes

import com.github.lamba92.kotlin.document.store.core.ObjectCollection
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import it.sagrabot.core.data.Sagra
import kotlinx.coroutines.Deferred

fun Routing.apiRoutes(sagreCollection: Deferred<ObjectCollection<Sagra>>) {
    route("api") {
        v1Routes(sagreCollection)
    }
}