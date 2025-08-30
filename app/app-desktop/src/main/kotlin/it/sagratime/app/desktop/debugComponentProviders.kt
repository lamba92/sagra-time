package it.sagratime.app.desktop

import it.sagratime.app.core.components.CoilComponentProvider

val debugComponentProviders = listOf(CoilComponentProvider {
    add(JarUriFetcherFactory)
})