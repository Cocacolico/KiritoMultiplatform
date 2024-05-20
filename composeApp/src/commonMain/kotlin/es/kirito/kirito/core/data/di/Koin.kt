package es.kirito.kirito.core.data.di

import es.kirito.kirito.login.domain.LoginRepository
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(commonModule(), platformModule())
    }

// called by iOS etc
fun initKoin() = initKoin() {}

fun commonModule() = module {
    //Algunas de las cosas que había en el ejemplo, por si nos dan ideas.
    //El viewmodel tendremos que meterlo así, claro.
  //  single { createJson() }
 //   single { createHttpClient(get(), get()) }

  //  viewModelOf(::LoginViewModel)
  //  single { LoginRepository() }
  //  single { FantasyPremierLeagueApi(get()) }

  //  single { AppSettings(get()) }
}
