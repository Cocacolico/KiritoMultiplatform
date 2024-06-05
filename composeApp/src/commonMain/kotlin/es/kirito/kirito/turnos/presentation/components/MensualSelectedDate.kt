package es.kirito.kirito.turnos.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import es.kirito.kirito.core.domain.util.enMiFormato
import es.kirito.kirito.core.domain.util.esTurnoConDias
import es.kirito.kirito.core.domain.util.isNotNullNorBlank
import es.kirito.kirito.core.domain.util.toLocalTime
import es.kirito.kirito.core.presentation.components.MyTextStd
import es.kirito.kirito.core.presentation.components.MyTextSubTitle
import es.kirito.kirito.core.presentation.components.MyTextTurno
import es.kirito.kirito.core.presentation.components.TextoResumenTurno
import es.kirito.kirito.core.presentation.theme.KiritoColors
import es.kirito.kirito.turnos.domain.MensualState
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.detalle_del_turno_seleccionado
import kirito.composeapp.generated.resources.detalles
import kirito.composeapp.generated.resources.exceso_de_jornada
import kirito.composeapp.generated.resources.festivo
import kirito.composeapp.generated.resources.mermas
import kirito.composeapp.generated.resources.nota__
import org.jetbrains.compose.resources.stringResource


@Composable
fun MensualSelectedDate(
    state: MensualState,
    onFestivoClick: () -> Unit,
    onMasDetallesClick: () -> Unit,
    onComjYLibraClick: () -> Unit,
    onExcesosClick: () -> Unit,
) {

    Column {
        Spacer(Modifier.padding(bottom = 8.dp))
        MyTextSubTitle(text = stringResource(Res.string.detalle_del_turno_seleccionado))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(alignment = Alignment.End)
        ) {
            MyTextStd(
                modifier = Modifier.weight(1f),
                text = state.selectedDate?.enMiFormato() ?: "",
                textAlign = TextAlign.Center
            )
            AnimatedVisibility(state.festivo != null) {
                Text(
                    text = stringResource(Res.string.festivo),
                    color = Color.Black,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .clickable { onFestivoClick() }
                        .background(color = KiritoColors().FESTIVO)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Button(onClick = { onMasDetallesClick() }) {
                Text(stringResource(Res.string.detalles))
            }
        }

        AnimatedVisibility(state.selectedCuDetalle != null) {
            MyTextTurno(
                state.selectedCuDetalle?.tipo,
                state.selectedCuDetalle?.turno,
            )
        }
        Spacer(Modifier.padding(bottom = 8.dp))

        AnimatedVisibility(state.selectedPrxTr != null) {
            MyTextStd(TextoResumenTurno(state.selectedPrxTr))
        }

        AnimatedVisibility(state.selectedCuDetalle?.esTurnoConDias() == true) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        onComjYLibraClick()
                    }) {
                val COMJs = state.selectedCuDetalle?.comj ?: 0
                val LIBRas = state.selectedCuDetalle?.libra ?: 0
                if (COMJs > 0)
                    LabelComj(COMJs)
                if (LIBRas > 0)
                    LabelLibra(LIBRas)
            }
        }


        AnimatedVisibility(state.selectedCuDetalle?.notas.isNotNullNorBlank()) {
            MyTextStd(
                stringResource(Res.string.nota__, state.selectedCuDetalle?.notas ?: "")
            )
        }

        AnimatedVisibility(
            !(state.selectedCuDetalle?.excesos == null || state.selectedCuDetalle.excesos == 0)
                    || !(state.selectedCuDetalle?.mermas == null || state.selectedCuDetalle.mermas == 0)
        ) {
            val mermas = state.selectedCuDetalle?.mermas ?: 0
            val excesos = state.selectedCuDetalle?.excesos ?: 0
            var texto = ""
            if (excesos > 0) {
                texto =
                    stringResource(Res.string.exceso_de_jornada) + " " + excesos.toLocalTime() + " "
            }
            if (mermas > 0) {
                texto += stringResource(Res.string.mermas) + " " + mermas.toLocalTime()
            }
            MyTextStd(text = texto, modifier = Modifier
                .clickable { onExcesosClick() }
                .fillMaxWidth()
                .padding(vertical = 12.dp))
        }
    }

}