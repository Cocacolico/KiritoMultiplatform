package es.kirito.kirito

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import es.kirito.kirito.core.data.database.DriverFactory
import es.kirito.kirito.core.data.database.KiritoDao
import es.kirito.kirito.core.data.sqldelight.KiritoDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val database = KiritoDatabase(DriverFactory(LocalContext.current).createDriver())
            val kiritoDao = KiritoDao(database)
            App(kiritoDao)
        }
    }
}

//@Preview
//@Composable
//fun AppAndroidPreview() {
//    App()
//}