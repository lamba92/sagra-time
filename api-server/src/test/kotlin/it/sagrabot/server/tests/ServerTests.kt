@file:OptIn(ExperimentalPathApi::class, ExperimentalTime::class)

package it.sagratime.server.tests

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
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import it.sagratime.core.data.GeoCoordinates
import it.sagratime.core.data.ItalianRegion
import it.sagratime.core.data.Location
import it.sagratime.core.data.Page
import it.sagratime.core.data.Event
import it.sagratime.server.ADMIN_PASSWORD
import it.sagratime.server.ADMIN_USERNAME
import it.sagratime.server.DB_PATH
import it.sagratime.server.SagraTime
import it.sagratime.server.getDocumentStoreSagraProvider
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
            setBody(TestEvent)
        }
        if (postResult.status != HttpStatusCode.OK) error("Failed to insert test sagra")
        val result = client.get("api/v1/sagra/search").body<Page<Event>>()
        assertEquals(listOf(TestEvent), result.results)
    }

    @Test
    fun canOnlyAddSagraWithAdminAuth() = testSagraApplication(withAuth = false) {
        val postResult = client.post("api/v1/sagra/add") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(TestEvent)
        }
        assertEquals(HttpStatusCode.Unauthorized, postResult.status)
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
        application { SagraTime(getDocumentStoreSagraProvider(store)) }
        block()
    }
}

val TestEvent =
    Event(
        name = "test",
        food = listOf("test1", "test2"),
        from = LocalDateTime.parse(Clock.System.now().toString().removeSuffix("Z")),
        until = LocalDateTime.parse((Clock.System.now() + 3.days).toString().removeSuffix("Z")),
        description = "test",
        location = Location(
            geoCoordinates = GeoCoordinates(1.0, 2.0),
            cityName = "test",
            region = ItalianRegion.Abruzzo
        )
    )