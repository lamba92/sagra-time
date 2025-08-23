@file:OptIn(ExperimentalPathApi::class, ExperimentalTime::class)

package it.sagrabot.server.tests

import com.github.lamba92.kotlin.document.store.core.use
import com.github.lamba92.kotlin.document.store.stores.leveldb.LevelDBStore
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import it.sagrabot.core.data.Coordinates
import it.sagrabot.core.data.ItalianRegion
import it.sagrabot.core.data.Location
import it.sagrabot.core.data.Page
import it.sagrabot.core.data.Sagra
import it.sagrabot.server.ADMIN_PASSWORD
import it.sagrabot.server.ADMIN_USERNAME
import it.sagrabot.server.DB_PATH
import it.sagrabot.server.SagraBot
import it.sagrabot.server.getDocumentStoreSagraProvider
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path
import kotlin.io.path.deleteRecursively
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.BeforeEach

class ServerTests {

    @BeforeEach
    fun clearDb() {
        Path(DB_PATH).deleteRecursively()
    }

    @Test
    fun insertAndRetrieve() = testSagraApplication {
        val postResult = client.post("api/v1/sagra/add") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(TestSagra)
        }
        if (postResult.status.value != 200) error("Failed to insert test sagra")
        val result = client.get("api/v1/sagra/search").body<Page<Sagra>>()
        assertEquals(listOf(TestSagra), result.results)
    }

    @Test
    fun canOnlyAddSagraWithAdminAuth() = testSagraApplication(withAuth = false) {
        val postResult = client.post("api/v1/sagra/add") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(TestSagra)
        }
        assertEquals(401, postResult.status.value)
    }
}

fun testSagraApplication(
    withAuth: Boolean = true,
    block: suspend ApplicationTestBuilder.() -> Unit
) = testApplication {
    client = createClient {
        install(ContentNegotiation) {
            json()
        }
        if (withAuth) {
            install(Auth) {
                basic {
                    credentials {
                        BasicAuthCredentials(ADMIN_USERNAME, ADMIN_PASSWORD)
                    }
                }
            }
        }
    }
    LevelDBStore.open(DB_PATH).use { store ->
        application { SagraBot(getDocumentStoreSagraProvider(store)) }
        block()
    }
}

val TestSagra =
    Sagra(
        name = "test",
        food = listOf("test1", "test2"),
        from = LocalDateTime.parse(Clock.System.now().toString().removeSuffix("Z")),
        until = LocalDateTime.parse((Clock.System.now() + 3.days).toString().removeSuffix("Z")),
        description = "test",
        location = Location(
            coordinates = Coordinates(1.0, 2.0),
            cityName = "test",
            region = ItalianRegion.Abruzzo
        )
    )