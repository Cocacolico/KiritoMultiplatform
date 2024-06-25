package es.kirito.kirito

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.DEBUG_PROPERTY_NAME
import kotlinx.coroutines.DEBUG_PROPERTY_VALUE_ON

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        System.setProperty(DEBUG_PROPERTY_NAME,DEBUG_PROPERTY_VALUE_ON)
   //     System.setProperty("kotlinx.coroutines.debug", "on")
        System.setProperty("kotlinx.coroutines.stacktrace.recovery", "true")

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
