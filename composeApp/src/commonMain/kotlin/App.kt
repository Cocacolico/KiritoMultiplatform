import androidx.compose.runtime.*
import es.kirito.kirito.core.data.database.KiritoDao
import es.kirito.kirito.core.presentation.theme.KiritoTheme
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview

import es.kirito.kirito.login.presentation.LoginScreen

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App(kiritoDao: KiritoDao) {
    KiritoTheme {
        LoginScreen(kiritoDao)

    }
}