package es.kirito.kirito.precarga.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun PrecargaScreen(navController: NavHostController) {

    val viewModel = koinViewModel<PrecargaViewModel>()

    val pasosCompletados by viewModel.pasosCompletados.collectAsState("")

    LaunchedEffect(pasosCompletados){
        if (pasosCompletados == "12"){
            navController.navigate("vistaHoy")
        }
    }


    Surface(Modifier.fillMaxSize()) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text("Precarga Screen", Modifier.padding(24.dp))
                Text("Pasos completados $pasosCompletados")
            }
        }


    }


}