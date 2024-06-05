package es.kirito.kirito.precarga.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import es.kirito.kirito.core.presentation.components.GetErrorString
import es.kirito.kirito.core.presentation.components.MyTextError
import es.kirito.kirito.core.presentation.components.MyTextStd
import es.kirito.kirito.core.presentation.theme.purpleRenfe
import es.kirito.kirito.core.presentation.theme.transparent
import es.kirito.kirito.precarga.domain.models.PreloadStep
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.aeroport
import kirito.composeapp.generated.resources.borrando_elementos_viejos
import kirito.composeapp.generated.resources.compose_multiplatform
import kirito.composeapp.generated.resources.descargando_elementos_generales
import kirito.composeapp.generated.resources.descargando_estaciones
import kirito.composeapp.generated.resources.descargando_festivos
import kirito.composeapp.generated.resources.descargando_grafico_actual
import kirito.composeapp.generated.resources.descargando_graficos
import kirito.composeapp.generated.resources.descargando_info_tiempo
import kirito.composeapp.generated.resources.descargando_mensajes_admins
import kirito.composeapp.generated.resources.descargando_teleindicadores
import kirito.composeapp.generated.resources.descargando_turnos
import kirito.composeapp.generated.resources.descargando_turnos_de_compa_eros
import kirito.composeapp.generated.resources.descargando_usuarios
import kirito.composeapp.generated.resources.finalizando_descarga
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun PrecargaScreen(navController: NavHostController) {

    val viewModel = koinViewModel<PrecargaViewModel>()

    val state by viewModel.state.collectAsState(PrecargaState())

    LaunchedEffect(state.elementBeingUpdated) {
        if (state.elementBeingUpdated == PreloadStep.FINISHED) {
            navController.navigate("vistaHoy")
        }
    }

    Surface(
        Modifier.fillMaxSize()

    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .paint(painterResource(Res.drawable.aeroport), contentScale = ContentScale.Crop)
        ) {
            Spacer(Modifier.weight(4f).background(transparent))
            MyTextStd(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(purpleRenfe),
                text = state.elementBeingUpdated.text(),
                textAlign = TextAlign.Center,
                color = Color.White
            )
            if (state.error != null) {
                if (state.error?.toIntOrNull() != null)
                    MyTextError(GetErrorString(state.error))
                else
                    MyTextError(state.error ?: "")
            }
            Spacer(Modifier.weight(1f).background(transparent))
        }
    }
}
