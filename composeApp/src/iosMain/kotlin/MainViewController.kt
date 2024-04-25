import androidx.compose.ui.window.ComposeUIViewController
import es.kirito.kirito.core.data.utils.ApplicationComponent


//Lo que había antes de tocar yo.
//fun MainViewController() = ComposeUIViewController { App() }




//Esta función es equivalente a la de arriba, pero me pedían que le añadiese la función
//initialize, esa que hace el ApplicationComponent.init(). Si asi no es como debe ser,
//pues mira a ver si lo solucionas. He puesto que devuelve el UIViewController por intuición, no
//porque sepa realmente qué debe devolver.
fun MainViewController(): UIViewController {

    fun initialize() {
        ApplicationComponent.init()
    }


    return ComposeUIViewController { App() }
}




//Debes añadir en iOSApp.swift el siguiente código:

@main
struct iOSApp: App {

    init() { MainViewControllerKt.initialize() }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
