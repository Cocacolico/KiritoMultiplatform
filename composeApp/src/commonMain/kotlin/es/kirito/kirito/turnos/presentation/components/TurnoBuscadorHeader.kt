package es.kirito.kirito.turnos.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.kirito.kirito.core.domain.util.deBinarioASemanal
import es.kirito.kirito.core.domain.util.toLocalTime
import es.kirito.kirito.core.presentation.components.MyTextStd
import es.kirito.kirito.core.presentation.components.MyTextStdWithPadding
import es.kirito.kirito.core.presentation.components.MyTextSubTitle
import es.kirito.kirito.core.presentation.theme.azulKirito
import es.kirito.kirito.turnos.domain.models.TurnoBuscadorDM
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.final__
import kirito.composeapp.generated.resources.hoy_hace_la_clave
import kirito.composeapp.generated.resources.inicio
import kirito.composeapp.generated.resources.notas
import kirito.composeapp.generated.resources.turno
import org.jetbrains.compose.resources.stringResource


@Composable
fun TurnoBuscadorHeader(turno: TurnoBuscadorDM, onCompiClicked: (Long) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                MyTextSubTitle(
                    text = stringResource( Res.string.turno) +
                            if (turno.equivalencia == null) " ${turno.turno}"
                            else " ${turno.turno} - ${turno.equivalencia}",
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                )
                MyTextStdWithPadding(
                    text = turno.diaSemana.deBinarioASemanal().toString(),
                    textAlign = TextAlign.End,
                    fontSize = 16.sp
                )
            }

            Row {
                MyTextStdWithPadding(
                    text = stringResource(Res.string.inicio),
                    textAlign = TextAlign.Center
                )
                MyTextStd(
                    text = "${turno.horaOrigen?.toLocalTime()} ${turno.sitioOrigen}",
                    textAlign = TextAlign.Start
                )
            }
            Row {
                MyTextStdWithPadding(
                    text = stringResource(Res.string.final__),

                    textAlign = TextAlign.Center
                )
                MyTextStd(
                    text = "${turno.horaFin?.toLocalTime()} ${turno.sitioFin}",
                    textAlign = TextAlign.Start
                )
            }
            if (turno.nombreCompi != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MyTextStdWithPadding(
                        modifier = Modifier.weight(1f),
                        text = stringResource(
                            Res.string.hoy_hace_la_clave,
                            turno.nombreCompi.toString()
                        )
                    )
                    IconButton(
                        onClick = { onCompiClicked(turno.idCompi ?: -1) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Call,
                            contentDescription = "",
                            tint = azulKirito,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
            if (turno.notas?.isNotEmpty() == true)
                turno.notas.let { nota ->
                    MyTextStd(
                        text = stringResource( Res.string.notas),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    MyTextStd(
                        text = nota.toString(),
                        modifier = Modifier.padding(start = 16.dp)
                    )

                }
        }
    }
}
