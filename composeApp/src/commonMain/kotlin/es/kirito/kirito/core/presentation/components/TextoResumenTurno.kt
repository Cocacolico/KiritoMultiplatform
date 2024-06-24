package es.kirito.kirito.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import es.kirito.kirito.core.domain.models.TurnoPrxTr
import es.kirito.kirito.core.domain.util.esTurnoDeTrabajo
import es.kirito.kirito.core.domain.util.hFinSietemil
import es.kirito.kirito.core.domain.util.hOrigenSietemil
import es.kirito.kirito.core.domain.util.toLocalTime
import es.kirito.kirito.core.presentation.utils.diaSemanaEntero
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.equivale_a___
import kirito.composeapp.generated.resources.este_turno_no_esta_en_
import kirito.composeapp.generated.resources.este_turno_no_esta_en_el_grafico
import org.jetbrains.compose.resources.stringResource


/**
 * Genera una frase del estilo: TODO ACEER 
 * **/
@Composable
fun TextoResumenTurno(entrada: TurnoPrxTr?): String {
    val turno = entrada ?: return ""
    var texto = ""
    if (turno.esTurnoDeTrabajo()) {
        if (turno.idGrafico == null || turno.idGrafico == 0L) {
            if (turno.tipo == "7000") {
                texto = texto.plus(
                    "" + turno.hOrigenSietemil() + " - " + turno.hFinSietemil()
                )
            } else {
                println("Este turno no tiene id_grafico ni es sietemil: $turno")
                texto = texto.plus(
                    stringResource(
                        Res.string.este_turno_no_esta_en_el_grafico
                    )
                )
            }
        } else if (turno.indicador == 0) {
            //Entonces el turno no pertenece a este día.
            texto = texto.plus(
                stringResource(Res.string.este_turno_no_esta_en_) +
                        turno.diaSemana.diaSemanaEntero()
            )
        } else {
            if (turno.equivalencia != null) {
                texto =
                    texto.plus(
                        stringResource(Res.string.equivale_a___, turno.equivalencia ?: "")
                    )
            }
            texto = texto.plus(
                " " + turno.sitioOrigen + " " + turno.horaOrigen?.toLocalTime() +
                        " - " + turno.sitioFin + " " + turno.horaFin?.toLocalTime()
            )
        }
    }
    return texto
    
}