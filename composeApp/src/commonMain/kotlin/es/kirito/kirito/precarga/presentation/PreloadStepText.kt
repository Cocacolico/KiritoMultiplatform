package es.kirito.kirito.precarga.presentation

import androidx.compose.runtime.Composable
import es.kirito.kirito.precarga.domain.models.PreloadStep
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.borrando_elementos_viejos
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
import org.jetbrains.compose.resources.stringResource


@Composable
fun PreloadStep.text(): String {
    return when(this){
        PreloadStep.BEGINNING -> ""
        PreloadStep.ELEMENTOS_VIEJOS -> stringResource(Res.string.borrando_elementos_viejos)
        PreloadStep.FESTIVOS -> stringResource(Res.string.descargando_festivos)
        PreloadStep.GRAFICOS -> stringResource(Res.string.descargando_graficos)
        PreloadStep.TURNOS -> stringResource(Res.string.descargando_turnos)
        PreloadStep.MENSAJES_ADMIN -> stringResource(Res.string.descargando_mensajes_admins)
        PreloadStep.ELEMENTOS_GENERALES -> stringResource(Res.string.descargando_elementos_generales)
        PreloadStep.TELEINDICADORES -> stringResource(Res.string.descargando_teleindicadores)
        PreloadStep.USUARIOS -> stringResource(Res.string.descargando_usuarios)
        PreloadStep.ESTACIONES -> stringResource(Res.string.descargando_estaciones)
        PreloadStep.GRAFICO_ACTUAL -> stringResource(Res.string.descargando_grafico_actual)
        PreloadStep.TURNOS_COMPIS -> stringResource(Res.string.descargando_turnos_de_compa_eros)
        PreloadStep.INFO_METEOROLOGICA -> stringResource(Res.string.descargando_info_tiempo)
        PreloadStep.NOTHING -> ""
        PreloadStep.FINISHED -> stringResource(Res.string.finalizando_descarga)
    }
}
