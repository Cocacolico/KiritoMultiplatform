package es.kirito.kirito.core.data.di

import es.kirito.kirito.core.domain.CoreRepository
import es.kirito.kirito.login.domain.LoginRepository
import es.kirito.kirito.login.presentation.LoginViewModel
import es.kirito.kirito.login.presentation.RegisterViewModel
import es.kirito.kirito.menu.domain.MenuRepository
import es.kirito.kirito.precarga.domain.PrecargaRepository
import es.kirito.kirito.precarga.presentation.PrecargaViewModel
import es.kirito.kirito.turnos.presentation.HoyViewModel
import es.kirito.kirito.turnos.presentation.MensualViewModel
import es.kirito.kirito.turnos.presentation.BuscadorViewModel
import es.kirito.kirito.menu.presentation.MenuViewModel
import es.kirito.kirito.turnos.domain.TurnosRepository
import org.koin.compose.viewmodel.dsl.viewModelOf

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
    //  single { FantasyPremierLeagueApi(get()) }
    //  single { AppSettings(get()) }


    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::PrecargaViewModel)
    viewModelOf(::HoyViewModel)
    viewModelOf(::MensualViewModel)
    viewModelOf(::BuscadorViewModel)
    viewModelOf(::MenuViewModel)


    single { CoreRepository() }
    single { LoginRepository() }
    single { PrecargaRepository() }
    single { TurnosRepository() }
    single { MenuRepository() }

}
