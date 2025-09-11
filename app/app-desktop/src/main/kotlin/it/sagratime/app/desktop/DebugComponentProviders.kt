package it.sagratime.app.desktop

import it.sagratime.app.core.components.CoilComponentProvider

val DebugComponentProviders =
    listOf(
        CoilComponentProvider {
            add(JarUriFetcherFactory)
        },
    )
