package it.sagratime.app.core.di

import it.sagratime.app.core.feature.router.Router
import org.koin.dsl.module

object DIModules {
    val core =
        module {
            single { Router() }
        }
    val viewModels =
        module {
        }
}
