package it.sagrabot.server

import com.github.lamba92.kotlin.document.store.stores.leveldb.LevelDBStore
import io.ktor.server.application.Application
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer

suspend fun main() {
    val store = LevelDBStore.open(DB_PATH)
    embeddedServer(
        factory = CIO,
        port = 8080
    ) {
      SagraBot(store)
    }.start(true)
    store.close()
}