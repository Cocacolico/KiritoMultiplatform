package es.kirito.kirito.turnos.domain.utils

import androidx.compose.runtime.Composable
import es.kirito.kirito.core.domain.models.CuDetalleConFestivoDBModel
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.la_empresa_te_da
import kirito.composeapp.generated.resources.por_hacer_este_turno
import kirito.composeapp.generated.resources.y_
import org.jetbrains.compose.resources.stringResource

@Composable
fun CuDetalleConFestivoDBModel.genComjYLibraString(): String {
    var texto = stringResource(Res.string.la_empresa_te_da)
    if (this.libra != null && this.libra!! > 0) {
        texto = texto.plus(this.libra.toString() + " LIBRa ")
        if (this.comj != null && this.comj!! > 0)
            texto = texto.plus(stringResource(Res.string.y_))
    }
    if (this.comj != null && this.comj!! > 0)
        texto = texto.plus(this.comj.toString() + " COMJ ")
    return stringResource(Res.string.por_hacer_este_turno, texto)
}