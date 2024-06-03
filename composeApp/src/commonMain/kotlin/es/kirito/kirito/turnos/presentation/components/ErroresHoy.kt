package es.kirito.kirito.turnos.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import es.kirito.kirito.core.domain.models.CuDetalleConFestivoDBModel
import es.kirito.kirito.core.domain.util.nombreLargoTiposDeTurnos
import es.kirito.kirito.core.presentation.components.BigTextInfo
import es.kirito.kirito.core.presentation.components.BigTextWarning
import es.kirito.kirito.core.presentation.utils.diaSemanaEntero
import es.kirito.kirito.turnos.domain.models.ErroresHoy
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.este_dia_tienes_un__en_un__
import kirito.composeapp.generated.resources.este_turno_no_esta_en_
import kirito.composeapp.generated.resources.este_turno_no_esta_en_el_grafico
import kirito.composeapp.generated.resources.este_turno_no_tiene_tareas
import kirito.composeapp.generated.resources.no_hay_grafico_para_este_dia
import kirito.composeapp.generated.resources.no_hay_un_turno_para_este_d_a_debes_crear_un_cuadro_anual
import org.jetbrains.compose.resources.stringResource

@Composable
fun ErroresHoy(
    errores: ErroresHoy,
    cuDetalle: CuDetalleConFestivoDBModel?,
    modifier: Modifier
) {
    Column(modifier.padding(vertical = 8.dp)) {
        if (errores.noHayCuDetalle)
            BigTextWarning(stringResource(Res.string.no_hay_un_turno_para_este_d_a_debes_crear_un_cuadro_anual))
        if (errores.noHayGraficoEnVigor)
            BigTextWarning(text = stringResource(Res.string.no_hay_grafico_para_este_dia))
        if (errores.elTurnoNoEstaEnElGrafico)
            BigTextWarning(text = stringResource(Res.string.este_turno_no_esta_en_el_grafico))
        if (errores.elTurnoNoTieneTareas)
            BigTextWarning(text = stringResource(Res.string.este_turno_no_tiene_tareas))
        if (errores.noEstaEnElDiaQueToca) {
            val diaSemana = cuDetalle?.getDiaSemana().diaSemanaEntero()
            BigTextWarning(text = stringResource(Res.string.este_turno_no_esta_en_) + diaSemana)
        }
        if (errores.elTurnoEsUnTipo && cuDetalle != null) {
            BigTextInfo(
                text =
                stringResource(
                    Res.string.este_dia_tienes_un__en_un__,
                    cuDetalle.turno.nombreLargoTiposDeTurnos(),
                    cuDetalle.tipo.nombreLargoTiposDeTurnos()
                )
            )
        }

    }
}