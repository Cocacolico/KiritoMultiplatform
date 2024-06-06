package es.kirito.kirito.turnos.presentation.utils

import androidx.compose.runtime.Composable
import es.kirito.kirito.core.domain.models.CuDetalleConFestivoDBModel
import es.kirito.kirito.core.domain.util.esTurnoConDias
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.la_empresa_te_da
import kirito.composeapp.generated.resources.por_hacer_este_turno
import kirito.composeapp.generated.resources.y_
import org.jetbrains.compose.resources.stringResource


@Composable
fun TextoExplicComjsYLibras(turno: CuDetalleConFestivoDBModel?): String {
    if (turno?.esTurnoConDias() == true) {
        var texto = stringResource(Res.string.la_empresa_te_da)
        if (turno.libra != null && turno.libra!! > 0) {
            texto = texto.plus(turno.libra.toString() + " LIBRa ")
            if (turno.comj != null && turno.comj!! > 0)
                texto = texto.plus(stringResource(Res.string.y_))
        }
        if (turno.comj != null && turno.comj!! > 0)
            texto = texto.plus(turno.comj.toString() + " COMJ ")
        return stringResource(Res.string.por_hacer_este_turno, texto)
    } else return ""
}