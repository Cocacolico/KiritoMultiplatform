import androidx.compose.ui.window.ComposeUIViewController
import es.kirito.kirito.core.data.utils.ApplicationComponent

fun MainViewController() =
    ComposeUIViewController {

        App()

    }


// Inicializamos ApplicationComponent en una función a parte, que luego llamamos de iOSApp.swift
fun initialize() {
    ApplicationComponent.init()
}



