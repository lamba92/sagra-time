package it.sagratime.app.core.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpRequestRetryConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.contentnegotiation.ContentNegotiationConfig
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.LoggingConfig
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import it.sagratime.core.data.Event
import it.sagratime.core.data.EventSearchQuery
import it.sagratime.core.data.EventsStatistics
import it.sagratime.core.data.Locale

class HttpEventRepository(
    override val endpoints: EventRepositoryV1Endpoints,
    private val httpClient: HttpClient = defaultClient(),
) : V1EventRepository {
    companion object {
        fun defaultClient(
            httpRequestRetryConfig: (HttpRequestRetryConfig.() -> Unit)? = {
                retryOnServerErrors(maxRetries = 3)
                exponentialDelay()
            },
            contentNegotiationConfig: (ContentNegotiationConfig.() -> Unit)? = { json() },
            loggingConfig: (LoggingConfig.() -> Unit)? = null,
        ) = HttpClient(CIO) {
            if (contentNegotiationConfig != null) {
                install(ContentNegotiation, contentNegotiationConfig)
            }
            if (loggingConfig != null) {
                install(Logging, loggingConfig)
            }
            if (httpRequestRetryConfig != null) {
                install(HttpRequestRetry, httpRequestRetryConfig)
            }
        }
    }

    override suspend fun getEventStatistics(): EventsStatistics = httpClient.get(endpoints.getEventStatisticsUrl()).body()

    override suspend fun getPopularSearches(locale: Locale): List<String> = httpClient.get(endpoints.getPopularSearchesUrl(locale)).body()

    override suspend fun searchCompletionQuery(
        query: String,
        locale: Locale,
    ): List<String> = httpClient.get(endpoints.searchCompletionQueryUrl(query, locale)).body()

    override suspend fun search(query: EventSearchQuery): List<Event> = httpClient.get(endpoints.searchUrl(query)).body()
}
