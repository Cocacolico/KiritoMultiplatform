import androidx.compose.ui.window.ComposeUIViewController
import es.kirito.kirito.core.data.database.DriverFactory
import es.kirito.kirito.core.data.database.KiritoDao
import es.kirito.kirito.core.data.sqldelight.KiritoDatabase
import es.kirito.kirito.core.data.utils.ApplicationComponent
import es.kirito.kirito.core.data.database.getDatabaseBuilderIos

fun MainViewController() =
    ComposeUIViewController {
        val databaseBuilder = getDatabaseBuilderIos()

        App(databaseBuilder)
        //App()
    }


// Inicializamos ApplicationComponent en una función a parte, que luego llamamos de iOSApp.swift
fun initialize() {
    ApplicationComponent.init()
}