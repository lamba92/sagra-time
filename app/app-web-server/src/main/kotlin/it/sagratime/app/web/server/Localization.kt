@file:OptIn(InternalResourceApi::class, ExperimentalResourceApi::class)

package it.sagratime.app.web.server

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.defaultForFileExtension
import io.ktor.server.request.path
import io.ktor.server.response.respondOutputStream
import io.ktor.server.routing.RoutingContext
import org.jetbrains.compose.resources.DensityQualifier
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.JvmResourceReader
import org.jetbrains.compose.resources.LanguageQualifier
import org.jetbrains.compose.resources.RegionQualifier
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.ResourceReader
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.ThemeQualifier
import org.jetbrains.compose.resources.getString
import java.util.Locale
import kotlin.jvm.java

suspend fun RoutingContext.serveResources() {
    val path = call.request.path()
    val resourcePath = path.removePrefix("/")
    val res =
        ComposeLocale::class.java.classLoader.getResourceAsStream(resourcePath)

    if (res == null) {
        call.response.status(HttpStatusCode.Companion.NotFound)
        return
    }

    val contentType =
        path
            .substringAfterLast('.', "")
            .takeIf { it.isNotEmpty() }
            ?.let { ContentType.Companion.defaultForFileExtension(it) }
    call.respondOutputStream(contentType) {
        res.use { input ->
            input.copyTo(this)
        }
    }
}

val SAFE_DEFAULT_RESOURCES_ENVIRONMENT =
    ResourceEnvironment(
        language = LanguageQualifier(""),
        region = RegionQualifier(""),
        theme = ThemeQualifier.LIGHT,
        density = DensityQualifier.HDPI,
    )

data class ComposeLocale(
    val language: LanguageQualifier,
    val region: RegionQualifier,
)

suspend fun getString(
    resource: StringResource,
    locales: List<ComposeLocale>,
    reader: ResourceReader = JvmResourceReader(ComposeLocale::class.java.classLoader),
): String {
    for ((language, region) in locales) {
        try {
            return getString(
                environment =
                    SAFE_DEFAULT_RESOURCES_ENVIRONMENT.copy(
                        language = language,
                        region = region,
                    ),
                resource = resource,
                reader = reader,
            )
        } catch (_: IllegalStateException) {
            continue
        }
    }
    error("No string resource found for $resource")
}

fun parseAcceptLanguage(header: String?): List<ComposeLocale> {
    if (header.isNullOrBlank()) return emptyList()
    return header
        .split(",")
        .map { it.trim() }
        .map { entry ->
            val parts = entry.split(";q=")
            val tag = parts[0].trim()
            val q = parts.getOrNull(1)?.toDoubleOrNull() ?: 1.0
            tag to q
        }.sortedByDescending { it.second }
        .map { Locale.forLanguageTag(it.first) }
        .map { ComposeLocale(LanguageQualifier(it.language), RegionQualifier(it.country)) }
}
