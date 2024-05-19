package es.kirito.kirito

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import es.kirito.kirito.core.data.database.getDatabaseBuilderAndroid

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val databaseBuilder = getDatabaseBuilderAndroid(this)
        setContent {
            App(databaseBuilder)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    val databaseBuilder = getDatabaseBuilderAndroid(LocalContext.current)

    App(databaseBuilder)
}