package it.sagratime.app.core.di

import it.sagratime.core.data.SystemLocaleProvider
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual fun getPlatformSpecificModule() =
    module {
        single {
            SystemLocaleProvider(
                scope = get(qualifier = named("appScope")),
                appContext = get(),
            )
        }
    }
