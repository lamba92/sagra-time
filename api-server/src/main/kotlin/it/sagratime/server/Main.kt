package it.sagratime.server

import com.github.lamba92.kotlin.document.store.core.DataStore
import com.github.lamba92.kotlin.document.store.core.KotlinDocumentStore
import com.github.lamba92.kotlin.document.store.core.getObjectCollection
import com.github.lamba92.kotlin.document.store.core.use
import com.github.lamba92.kotlin.document.store.stores.leveldb.LevelDBStore
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import it.sagratime.core.data.Event
import it.sagratime.server.service.KotlinDocumentStoreEventProvider

suspend fun main() {
    LevelDBStore.open(DB_PATH).use { store ->
        embeddedServer(
            factory = CIO,
            port = 8080
        ) {
            SagraTime(getDocumentStoreSagraProvider(store))
        }.start(true)
    }
}

suspend fun getDocumentStoreSagraProvider(store: DataStore): KotlinDocumentStoreEventProvider {
    val db = KotlinDocumentStore(store)
    val collection = db.getObjectCollection<Event>("sagre")
    val provider = KotlinDocumentStoreEventProvider(collection)
    return provider
}
