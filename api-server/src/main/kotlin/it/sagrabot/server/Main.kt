package it.sagrabot.server

import com.github.lamba92.kotlin.document.store.core.DataStore
import com.github.lamba92.kotlin.document.store.core.KotlinDocumentStore
import com.github.lamba92.kotlin.document.store.core.getObjectCollection
import com.github.lamba92.kotlin.document.store.core.use
import com.github.lamba92.kotlin.document.store.stores.leveldb.LevelDBStore
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import it.sagrabot.core.data.Sagra
import it.sagrabot.server.service.KotlinDocumentStoreSagraProvider

suspend fun main() {
    LevelDBStore.open(DB_PATH).use { store ->
        embeddedServer(
            factory = CIO,
            port = 8080
        ) {
            SagraBot(getDocumentStoreSagraProvider(store))
        }.start(true)
    }
}

suspend fun getDocumentStoreSagraProvider(store: DataStore): KotlinDocumentStoreSagraProvider {
    val db = KotlinDocumentStore(store)
    val collection = db.getObjectCollection<Sagra>("sagre")
    val provider = KotlinDocumentStoreSagraProvider(collection)
    return provider
}
