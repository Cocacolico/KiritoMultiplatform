package es.kirito.kirito.precarga.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun PrecargaScreen(navController: NavHostController) {

    val viewModel = koinViewModel<PrecargaViewModel>()
    val timer by viewModel.timer.collectAsState()

    LaunchedEffect(timer) {
        if (timer == 3)
            navController.navigate("kirito") {
                // De esta forma eliminamos el backstack anterior hasta la screen de Login
                popUpTo("login") { inclusive = true }
            }
    }

    Surface(Modifier.fillMaxSize()) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text("Precargando lo precargable")
                Text("Avanzamos a la siguiente screen en: $timer")
            }
        }


    }


}