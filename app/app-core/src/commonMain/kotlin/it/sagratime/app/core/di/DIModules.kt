package it.sagratime.app.core.di

import it.sagratime.app.core.feature.cards.search.SearchCardViewModel
import it.sagratime.app.core.feature.cards.welcome.WelcomeCardViewModel
import it.sagratime.app.core.feature.router.SagraTimeRouter
import it.sagratime.app.core.repository.LocaleService
import it.sagratime.app.core.repository.LocationService
import it.sagratime.app.core.repository.MockLocationService
import it.sagratime.app.core.repository.MockSagreRepository
import it.sagratime.app.core.repository.SagreRepository
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object DIModules {
    val core =
        module {
            single { SagraTimeRouter() }
            single { LocaleService() }
        }
    val viewModels =
        module {
            viewModelOf(::WelcomeCardViewModel)
            viewModelOf(::SearchCardViewModel)
        }

    val mocks =
        module {
            single<SagreRepository> { MockSagreRepository }
            single<LocationService> { MockLocationService }
        }
}
