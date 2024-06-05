package es.kirito.kirito.turnos.presentation.utils

import androidx.compose.runtime.Composable
import es.kirito.kirito.core.domain.models.CuDetalleConFestivoDBModel
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.excediste_x_horas_y_generaste_x
import kirito.composeapp.generated.resources.tu_descanso_se_vio_mermado_en_x_horas_y_generaste_x
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.stringResource


@Composable
fun TextoExplicMermaGenerada(turno: CuDetalleConFestivoDBModel?): String {
    val excesos = turno?.excesos ?: 0
    val mermas = turno?.mermas ?: 0
    var textoE = ""
    var textoM = ""
    var texto = ""
    if (excesos != 0) {
        textoE = stringResource(Res.string.excediste_x_horas_y_generaste_x,
            LocalTime.fromSecondOfDay((excesos / 1.65).toInt()),
            LocalTime.fromSecondOfDay(excesos)
        )
        texto = textoE + "\n"
    }

    if (mermas != 0) {
        textoM = stringResource(Res.string.tu_descanso_se_vio_mermado_en_x_horas_y_generaste_x,
            LocalTime.fromSecondOfDay((mermas / 1.65).toInt()),
            LocalTime.fromSecondOfDay(mermas)
        )
        texto += textoM
    }

    return texto.trimEnd()
}