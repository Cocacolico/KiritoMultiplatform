package es.kirito.kirito.turnos.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import es.kirito.kirito.core.domain.models.GrTareaConClima
import es.kirito.kirito.core.domain.util.fromHtmlWithBreaksToSpanned
import es.kirito.kirito.core.domain.util.isBefore
import es.kirito.kirito.core.domain.util.textoServicio
import es.kirito.kirito.core.domain.util.toInstant
import es.kirito.kirito.core.domain.util.toLocalDate
import es.kirito.kirito.core.domain.util.toLocalTime
import es.kirito.kirito.core.presentation.components.MyTextStdWithPadding
import es.kirito.kirito.core.presentation.components.MyTextTareas
import es.kirito.kirito.core.presentation.theme.customColors
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn


@Composable
fun TareaConClima(tarea: GrTareaConClima, modifier: Modifier, onClick: (GrTareaConClima) -> Unit) {
    Column(
        modifier
            .fillMaxWidth()
            .clickable { onClick(tarea) }
            .background(color = tarea.calcBackgroundColor())

    ) {
        HorizontalDivider()
        Row(Modifier.fillMaxWidth()) {
            MyTextTareas(
                text = tarea.textoServicio(),
                modifier = Modifier.weight(1.2f).padding(end = 4.dp)
            )
            MyTextTareas(
                text = tarea.sitioOrigen.toString(),
                modifier = Modifier.weight(1.5f).padding(end = 4.dp)
            )
            MyTextTareas(
                text = tarea.horaOrigen?.toLocalTime().toString(),
                modifier = Modifier.weight(1f).padding(end = 4.dp)
            )
            MyTextTareas(
                text = tarea.sitioFin.toString(),
                modifier = Modifier.weight(1.5f).padding(end = 4.dp)
            )
            MyTextTareas(
                text = tarea.horaFin?.toLocalTime().toString(),
                modifier = Modifier.weight(1f)
            )
            IconClimaWithTemp(tarea)
        }
        if (tarea.notasTren != null)
            MyTextStdWithPadding(
                text = tarea.notasTren.fromHtmlWithBreaksToSpanned()//.toAnnotatedString()
            )
    }
}

@Composable
private fun GrTareaConClima.calcBackgroundColor(): Color {
    val dtInicio = this.inserted?.toInstant() ?: return Color.Transparent
    val date = dtInicio.toLocalDate()


    if (this.horaOrigen != null && this.horaFin != null &&
        this.horaOrigen?.toLocalTime()!! <= Clock.System.now().toLocalTime() && this.horaFin?.toLocalTime()!! >= Clock.System.now().toLocalTime() &&
        date == Clock.System.todayIn(TimeZone.currentSystemDefault())
    ) {//Está en la hora actual:
        return MaterialTheme.customColors.colorSecondaryVariant
    }
    //No es la misma hora, ¿Es 12 horas antes?
    else if (this.horaOrigen != null && this.horaFin != null && date == Clock.System.todayIn(TimeZone.currentSystemDefault())) {
        var diferencia = this.horaOrigen!! - dtInicio.toLocalTime().toSecondOfDay()
        if (diferencia < 0)
            diferencia += 86400
        val dtInicioTarea = dtInicio.plus(diferencia, DateTimeUnit.SECOND)
        if (dtInicioTarea.isBefore(Clock.System.now())) {
            //Es una tarea anterior:
            return MaterialTheme.customColors.fondoTareaPasada
        }
    }
    return Color.Transparent
}