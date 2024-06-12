package es.kirito.kirito.core.presentation.components.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import es.kirito.kirito.core.data.database.GrTareas
import es.kirito.kirito.core.data.database.OtColoresTrenes
import es.kirito.kirito.core.presentation.components.ImagenEga
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDialogImagenEga(
    horaOrigen: Long,
    horaFin: Long,
    tareasCortas: List<GrTareas>,
    dia: Long?,
    coloresTrenes: List<OtColoresTrenes>?,
    initialScroll: Int? = 0,
    factorZoom: Int? = 0,
    onDismiss: ()->Unit,
) {
    BasicAlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        content = {
            Card(
                Modifier.wrapContentSize(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Box(modifier = Modifier.padding(8.dp)) {
                    ImagenEga(
                        timeInicio = horaOrigen,
                        timeFin = horaFin,
                        tareas = tareasCortas,
                        dia = dia,
                        coloresTrenes = coloresTrenes,
                        initialScroll = initialScroll ?: 0,
                        factorZoom = factorZoom ?: 5,
                    ) { _, _ ->
                        onDismiss()
                    }
                }
            }
        }
    )
}