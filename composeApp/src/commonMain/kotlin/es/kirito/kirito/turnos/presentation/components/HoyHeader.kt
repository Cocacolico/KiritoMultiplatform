package es.kirito.kirito.turnos.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import es.kirito.kirito.core.domain.models.CuDetalleConFestivoDBModel
import es.kirito.kirito.core.domain.models.TurnoPrxTr
import es.kirito.kirito.core.domain.util.hFinSietemil
import es.kirito.kirito.core.domain.util.hOrigenSietemil
import es.kirito.kirito.core.domain.util.toLocalTime
import es.kirito.kirito.core.presentation.components.MyTextStdWithPadding
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.final_
import kirito.composeapp.generated.resources.origen
import org.jetbrains.compose.resources.stringResource


@Composable
fun TurnoProxTarHeader(turno: TurnoPrxTr?, sietemil: CuDetalleConFestivoDBModel?) {

    val hOrigen = turno?.horaOrigen?.toLocalTime() ?: sietemil?.turno?.hOrigenSietemil()
    val hFin = turno?.horaFin?.toLocalTime() ?: sietemil?.turno?.hFinSietemil()
    val sOrigen = turno?.sitioOrigen
    val sFin = turno?.sitioFin

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()

        ) {

            Row(Modifier.fillMaxWidth()) {
                MyTextStdWithPadding(
                    text = stringResource(Res.string.origen),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                MyTextStdWithPadding(
                    text = stringResource(Res.string.final_),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )


            }
            Row(Modifier.fillMaxWidth()) {
                if (sOrigen != null)
                    Text(
                        text = sOrigen.toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                Text(
                    text = hOrigen.toString(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                if (sFin != null)
                    Text(
                        text = sFin.toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                Text(
                    text = hFin.toString(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
