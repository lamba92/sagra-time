@file:OptIn(ExperimentalResourceApi::class, InternalResourceApi::class)

package it.sagratime.app.web.server

import io.ktor.server.html.respondHtml
import io.ktor.server.routing.RoutingContext
import it.sagratime.app_core.generated.resources.Res
import it.sagratime.app_core.generated.resources.app_name
import it.sagratime.app_core.generated.resources.welcome_card_subtitle
import it.sagratime.app_core.generated.resources.welcome_card_title
import it.sagratime.app_core.generated.resources.what_is_a_sagra_community_snippet_description
import it.sagratime.app_core.generated.resources.what_is_a_sagra_community_snippet_title
import it.sagratime.app_core.generated.resources.what_is_a_sagra_culture_snippet_description
import it.sagratime.app_core.generated.resources.what_is_a_sagra_culture_snippet_title
import it.sagratime.app_core.generated.resources.what_is_a_sagra_land_identity_snippet_description
import it.sagratime.app_core.generated.resources.what_is_a_sagra_land_identity_snippet_title
import it.sagratime.app_core.generated.resources.what_is_a_sagra_subtitle
import it.sagratime.app_core.generated.resources.what_is_a_sagra_title
import it.sagratime.app_core.generated.resources.what_is_a_sagra_tradition_snippet_description
import it.sagratime.app_core.generated.resources.what_is_a_sagra_tradition_snippet_title
import kotlinx.html.BODY
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.lang
import kotlinx.html.p
import kotlinx.html.script
import kotlinx.html.style
import kotlinx.html.title
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi

suspend fun RoutingContext.getHomeBootstrapDocument(locales: List<ComposeLocale>) {
    val strings =
        listOf(
            Res.string.welcome_card_title,
            Res.string.welcome_card_subtitle,
            Res.string.what_is_a_sagra_title,
            Res.string.what_is_a_sagra_subtitle,
            Res.string.what_is_a_sagra_tradition_snippet_title,
            Res.string.what_is_a_sagra_tradition_snippet_description,
            Res.string.what_is_a_sagra_community_snippet_title,
            Res.string.what_is_a_sagra_community_snippet_description,
            Res.string.what_is_a_sagra_land_identity_snippet_title,
            Res.string.what_is_a_sagra_land_identity_snippet_description,
            Res.string.what_is_a_sagra_culture_snippet_title,
            Res.string.what_is_a_sagra_culture_snippet_description,
        ).map { getString(resource = it, locales = locales) }
    getBootstrapDocument(
        getString(
            resource = Res.string.app_name,
            locales = locales,
        ),
        locales,
    ) {
        strings.forEach { string ->
            p { text(string) }
        }
    }
}

suspend fun RoutingContext.getBootstrapDocument(
    title: String,
    locales: List<ComposeLocale>,
    executableName: String = "app-web-compose.js",
    content: BODY.() -> Unit = {},
) = call.respondHtml {
    lang = locales.firstOrNull()?.let { "${it.language.language}-${it.region.region}" } ?: "en-US"
    head {
        title(title)
        script {
            defer = true
            src = executableName
        }
    }
    body {
        style =
            "margin: 0; overflow: hidden; width: 100vw; height: 100vh; color: transparent !important"
        content()
    }
}
