package es.kirito.kirito.turnos.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.kirito.kirito.core.domain.models.GrTareaConClima
import es.kirito.kirito.core.domain.util.fromHtmlWithBreaksToSpanned
import es.kirito.kirito.core.domain.util.isNotNullNorBlank
import es.kirito.kirito.core.domain.util.textoServicio
import es.kirito.kirito.core.domain.util.toLocalTime
import es.kirito.kirito.core.presentation.components.MyTextStdWithPadding


@Composable
fun TareasDeTurnoBuscador(tareas: List<GrTareaConClima>) {

    Column(modifier = Modifier.fillMaxWidth()) {
        tareas.forEachIndexed { index, tarea ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = tarea.textoServicio(),
                    maxLines = 2,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .weight(1F)
                        .padding(start = 8.dp)
                )
                Text(
                    text = tarea.sitioOrigen ?: "",
                    maxLines = 2,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .weight(1.5F)
                        .padding(start = 8.dp)
                )
                Text(
                    text = tarea.horaOrigen?.toLocalTime().toString(),
                    maxLines = 2,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .weight(1F)
                        .padding(start = 8.dp)
                )
                Text(
                    text = tarea.sitioFin ?: "",
                    maxLines = 2,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .weight(1.5F)
                        .padding(start = 8.dp)
                )
                Text(
                    text = tarea.horaFin?.toLocalTime().toString(),
                    maxLines = 2,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .weight(1F)
                        .padding(start = 8.dp, end = 16.dp)
                )
            }
            if (tarea.notasTren.isNotNullNorBlank())
                MyTextStdWithPadding(
                    text = tarea.notasTren.fromHtmlWithBreaksToSpanned()
                )
            if (index != tareas.lastIndex)
                HorizontalDivider(modifier = Modifier)
            else
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
        }

    }
}
