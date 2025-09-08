package it.sagratime.app.core.di

import it.sagratime.app.core.feature.cards.search.SearchCardViewModel
import it.sagratime.app.core.feature.cards.welcome.WelcomeCardViewModel
import it.sagratime.app.core.feature.home.HomeViewModel
import it.sagratime.app.core.feature.router.SagraTimeRouter
import it.sagratime.app.core.repository.EventRepository
import it.sagratime.app.core.repository.LocaleService
import it.sagratime.app.core.repository.LocationService
import it.sagratime.app.core.repository.mocks.MockEventRepository
import it.sagratime.app.core.repository.mocks.MockLocationService
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
            viewModelOf(::HomeViewModel)
            viewModelOf(::WelcomeCardViewModel)
            viewModelOf(::SearchCardViewModel)
        }

    val mocks =
        module {
            single<EventRepository> { MockEventRepository }
            single<LocationService> { MockLocationService }
        }
}
