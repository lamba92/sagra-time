package it.sagratime.app.core.di

import it.sagratime.app.core.feature.cards.welcome.WelcomeCardViewModel
import it.sagratime.app.core.feature.router.SagraTimeRouter
import it.sagratime.app.core.repository.MockSagreRepository
import it.sagratime.app.core.repository.SagreRepository
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object DIModules {
    val core =
        module {
            single { SagraTimeRouter() }
        }
    val viewModels =
        module {
            viewModelOf(::WelcomeCardViewModel)
        }

    val mocks =
        module {
            single<SagreRepository> { MockSagreRepository }
        }
}
