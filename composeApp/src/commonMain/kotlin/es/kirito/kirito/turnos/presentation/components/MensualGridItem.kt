package es.kirito.kirito.turnos.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import es.kirito.kirito.core.domain.util.colorDeFondoTurnos
import es.kirito.kirito.core.domain.util.colorTextoTurnos
import es.kirito.kirito.core.domain.util.minusEndTime
import es.kirito.kirito.core.domain.util.toComposeColor
import es.kirito.kirito.core.presentation.theme.KiritoColors
import es.kirito.kirito.core.presentation.theme.Orange
import es.kirito.kirito.turnos.domain.models.CuDetalleConFestivoSemanal
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

@Composable
fun MensualGridItem(turno: CuDetalleConFestivoSemanal) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = turno.fecha.dayOfMonth.toString(),
            textAlign = TextAlign.Center,
            modifier = Modifier.background(
                color = if (turno.idFestivo == null) Color.LightGray
                else Color(0xffFFFF99)
            ).border(
                width = if (turno.fecha == Clock.System.todayIn(TimeZone.currentSystemDefault()))
                    3.dp else 0.dp, color =
                if (turno.fecha == Clock.System.todayIn(TimeZone.currentSystemDefault())) Color.Red
                else Color.Transparent
            ).fillMaxWidth()
        )

        Box {
            Text(
                text = turno.turno, color = colorTextoTurnos(turno.tipo),
                textAlign = TextAlign.Center,
                modifier = Modifier.background(colorDeFondoTurnos(turno.tipo).toComposeColor())
                    .fillMaxWidth()

            )

            Canvas(modifier = Modifier.matchParentSize()) {
                if(turno.notas.isNotBlank()){
                    Path().apply {
                        moveTo(0f, 0f)
                        lineTo(size.width / 4, 0f)
                        lineTo(0f, size.height / 3)
                        close()
                    }.let { path ->
                        drawPath(
                            path = path,
                            color = Orange
                        )
                    }
                }
                if (turno.comj != 0){
                    Path().apply {
                        moveTo(size.width, 0f)
                        lineTo(3 * size.width / 5, 0f)
                        lineTo(size.width, size.height / 2)
                        close()
                    }.let { path ->
                        drawPath(
                            path = path,
                            color = KiritoColors().COMJ
                        )
                    }
                }
                if (turno.libra != 0){
                    Path().apply {
                        moveTo(size.width, 0f)
                        lineTo(3 * size.width / 4, 0f)
                        lineTo(size.width, size.height / 3)
                        close()
                    }.let { path ->
                        drawPath(
                            path = path,
                            color = KiritoColors().LIBRa
                        )
                    }
                }

            }
        }

        Box(Modifier.align(Alignment.CenterHorizontally)){
            //Linea de DCOMs.


            var color: Int = Color.Transparent.toArgb()
            var width = 0f
            val step = 0.018f
            val horaInicio = turno.horaInicio
            val horaFin = turno.horaFin
            if (horaInicio != null && turno.idDetalle != 0L) {
                color =  turno.color?.toInt() ?: 0

                if (horaFin != null){
                    val factor = 4L//Con este factor, obtengo cuartos de hora en vez de horas completas.
                    val horas = (horaInicio.minusEndTime(horaFin) / (3600/factor))
                        .coerceAtMost(12*factor)
                        .coerceAtLeast(2*factor)

                    width = horas  * step

                }
            }

            println("el ancho es $width y color es $color original ${turno.color}")
            //Linea inferior en medio.
            Box(
                modifier = Modifier
                    .fillMaxWidth(width)
                    .height(4.dp)

            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    drawRect(color = Color(color.toLong()))
                }
            }
        }


    }
}