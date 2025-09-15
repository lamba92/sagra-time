package it.sagratime.app.core.di

import it.sagratime.app.core.feature.cards.search.SearchCardViewModel
import it.sagratime.app.core.feature.cards.welcome.WelcomeCardViewModel
import it.sagratime.app.core.feature.home.HomeViewModel
import it.sagratime.app.core.feature.router.SagraTimeRouter
import it.sagratime.app.core.locale.LocaleService
import it.sagratime.app.core.repository.LocationService
import it.sagratime.app.core.repository.mocks.MockEventRepository
import it.sagratime.app.core.repository.mocks.MockLocationService
import it.sagratime.core.repository.V1EventRepository
import kotlinx.coroutines.CoroutineScope
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

object DIModules {
    val core =
        module {
            single { SagraTimeRouter() }
            single { LocaleService(get(), get(named("appScope"))) }
        }

    val viewModels =
        module {
            viewModelOf(::HomeViewModel)
            viewModelOf(::WelcomeCardViewModel)
            viewModelOf(::SearchCardViewModel)
        }

    val mocks =
        module {
            single<V1EventRepository> { MockEventRepository }
            single<LocationService> { MockLocationService }
        }

    val platformSpecific = getPlatformSpecificModule()
}

internal expect fun getPlatformSpecificModule(): Module

fun KoinApplication.sagraTimeModules(
    appScope: CoroutineScope,
    withMocks: Boolean = false,
) {
    modules(
        module {
            single(named("appScope")) { appScope }
        },
    )

    modules(
        listOf(
            DIModules.core,
            DIModules.viewModels,
            DIModules.platformSpecific,
        ),
    )

    if (withMocks) modules(DIModules.mocks)
}
