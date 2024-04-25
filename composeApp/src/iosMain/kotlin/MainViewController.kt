import androidx.compose.ui.window.ComposeUIViewController
import es.kirito.kirito.core.data.utils.ApplicationComponent


//Lo que había antes de tocar yo.
//fun MainViewController() = ComposeUIViewController { App() }



fun MainViewController() {

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
