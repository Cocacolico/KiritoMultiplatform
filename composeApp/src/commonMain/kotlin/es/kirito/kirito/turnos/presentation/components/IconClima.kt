package es.kirito.kirito.turnos.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.kirito.kirito.core.data.constants.MyConstants
import es.kirito.kirito.core.domain.models.GrTarea
import es.kirito.kirito.core.domain.util.toStringIfNull
import es.kirito.kirito.core.presentation.theme.Orange
import es.kirito.kirito.core.presentation.theme.Red
import es.kirito.kirito.core.presentation.theme.Violet
import es.kirito.kirito.core.presentation.theme.amarilloKirito
import es.kirito.kirito.core.presentation.theme.azulKirito
import es.kirito.kirito.core.presentation.theme.azulKiritoClaro
import es.kirito.kirito.core.presentation.theme.verdeOk
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.cloud_rain_alt_1_svgrepo_com
import kirito.composeapp.generated.resources.cloud_sun_svgrepo_com
import kirito.composeapp.generated.resources.cloud_svgrepo_com
import kirito.composeapp.generated.resources.empty_24
import kirito.composeapp.generated.resources.mist_svgrepo_com
import kirito.composeapp.generated.resources.snow_svgrepo_com
import kirito.composeapp.generated.resources.sun_svgrepo_com
import kirito.composeapp.generated.resources.wind_svgrepo_com
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt


@Composable
fun IconClima(
    modifier: Modifier = Modifier,
    mmLluvia: Float,
    cmNieve: Float,
    probPrecip: Int,
    visibilidad: Int,
    viento: Float,
    nublado: Int,
) {

    Icon(
        modifier = modifier,
        painter = painterResource(
            resource = if (cmNieve != 0f)
                Res.drawable.snow_svgrepo_com
            else if (mmLluvia != 0f || probPrecip > 50)
                Res.drawable.cloud_rain_alt_1_svgrepo_com
            else if (visibilidad in 1..MyConstants.LOW_VISIBILITY)
                Res.drawable.mist_svgrepo_com
            else if (viento > MyConstants.FAST_WIND)
                Res.drawable.wind_svgrepo_com
            else if (nublado > 65)
                Res.drawable.cloud_svgrepo_com
            else if (nublado > 15)
                Res.drawable.cloud_sun_svgrepo_com
            else
                Res.drawable.sun_svgrepo_com
        ), contentDescription = ""
    )
}


@Composable
fun IconClimaWithTemp(tarea: GrTarea) {
    Box(modifier = Modifier.wrapContentSize()) {
        if (tarea.lluviaF != null && tarea.nubladoF != null
            && tarea.vientoF != null && tarea.nieveF != null
            && tarea.probF != null && tarea.visibilidadF != null
        )
            IconClima(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(end = 4.dp, bottom = 4.dp),
                mmLluvia = tarea.lluviaF!!,
                cmNieve = tarea.nieveF!!,
                probPrecip = tarea.probF!!,
                visibilidad = tarea.visibilidadF!!,
                viento = tarea.vientoF!!,
                nublado = tarea.nubladoF!!
            )
        else
            Icon(
                painter = painterResource(Res.drawable.empty_24),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(end = 4.dp, bottom = 4.dp)
            )
        Text(
            text = tarea.tempF?.roundToInt().toStringIfNull(""),
            color = when (tarea.tempF ?: 0f) {
                in -20f..0f -> azulKirito
                in 0f..10f -> azulKiritoClaro
                in 10f..17f -> verdeOk
                in 17f..25f -> amarilloKirito
                in 25f..32f ->Orange
                in 32f..60f -> Red
                else ->  Violet
            }, modifier = Modifier.align(Alignment.BottomEnd),
            fontSize = 10.sp,
            style = LocalTextStyle.current.copy(lineHeight = TextUnit.Unspecified)
        )
    }
}

