@file:OptIn(ExperimentalSerializationApi::class)

package it.sagratime.app.core.repository

import io.ktor.http.ParametersBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import io.ktor.http.appendPathSegments
import io.ktor.http.buildUrl
import it.sagratime.core.UrlParameters
import it.sagratime.core.data.EventSearchQuery
import it.sagratime.core.data.Locale
import it.sagratime.core.data.SearchCompletionQuery
import it.sagratime.core.encodeToParameters
import kotlinx.serialization.ExperimentalSerializationApi

class EventRepositoryV1Endpoints(
    val host: String,
    val protocol: URLProtocol,
    val port: Int = protocol.defaultPort,
    val urlParameters: UrlParameters = UrlParameters.Default,
) {
    companion object {
        val LOCALHOST
            get() = EventRepositoryV1Endpoints("localhost", URLProtocol.Companion.HTTP)
    }

    private fun buildV1Url(
        vararg pathSegments: String,
        parametersBlock: ParametersBuilder.() -> Unit = {},
    ) = buildUrl {
        protocol = this@EventRepositoryV1Endpoints.protocol
        port = this@EventRepositoryV1Endpoints.port
        host = this@EventRepositoryV1Endpoints.host
        appendPathSegments(listOf("api", "v1") + pathSegments.toList())
        parameters.apply(parametersBlock)
    }

    fun getEventStatisticsUrl(): Url = buildV1Url("events", "statistics")

    fun getPopularSearchesUrl(locale: Locale): Url =
        buildV1Url("events", "popular") {
            appendAll(urlParameters.encodeToParameters(locale))
        }

    fun searchCompletionQueryUrl(query: SearchCompletionQuery): Url =
        buildV1Url("events", "autocomplete") {
            appendAll(urlParameters.encodeToParameters(query))
        }

    fun searchUrl(query: EventSearchQuery): Url =
        buildV1Url("events", "search") {
            appendAll(urlParameters.encodeToParameters(query))
        }
}
