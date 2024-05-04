import androidx.compose.ui.window.ComposeUIViewController
import es.kirito.kirito.core.data.database.DriverFactory
import es.kirito.kirito.core.data.database.KiritoDao
import es.kirito.kirito.core.data.sqldelight.KiritoDatabase
import es.kirito.kirito.core.data.utils.ApplicationComponent

fun MainViewController() = ComposeUIViewController {
    val database = KiritoDatabase(DriverFactory().createDriver())
    val kiritoDao = KiritoDao(database)
    App(kiritoDao)
}
//https://medium.com/@hunterfreas/sqldelight-setup-a-local-database-for-kmp-compose-ios-android-65f7e2b1e224
//Héctor!! Hay un paso que debes hacer en Xcode para que vaya sqlDelight, mira en esa web, por la mitad,
// donde dice Xcode setup.


// Inicializamos ApplicationComponent en una función a parte, que luego llamamos de iOSApp.swift
fun initialize() {
    ApplicationComponent.init()
}