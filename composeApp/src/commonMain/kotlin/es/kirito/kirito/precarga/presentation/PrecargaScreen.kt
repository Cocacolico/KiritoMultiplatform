package es.kirito.kirito.precarga.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun PrecargaScreen(navController: NavHostController) {

    val viewModel = koinViewModel<PrecargaViewModel>()

    Surface(Modifier.fillMaxSize()) {
        Text("Precargando lo precargable")

    }


}