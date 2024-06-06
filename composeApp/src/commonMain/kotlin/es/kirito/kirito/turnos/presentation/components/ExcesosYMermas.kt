package es.kirito.kirito.turnos.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import es.kirito.kirito.core.domain.models.CuDetalleConFestivoDBModel
import es.kirito.kirito.core.domain.util.toLocalTime
import es.kirito.kirito.core.presentation.components.MyTextStd
import es.kirito.kirito.core.presentation.components.ParagraphSubtitle
import es.kirito.kirito.core.presentation.theme.azulKirito
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.exceso_de_jornada
import kirito.composeapp.generated.resources.excesos
import kirito.composeapp.generated.resources.mermas
import org.jetbrains.compose.resources.stringResource

@Composable
fun ExcesosYMermas(cuDetalle: CuDetalleConFestivoDBModel?, onClick: () -> Unit) {
    if (cuDetalle?.excesos != null && cuDetalle.mermas != null) {
        Column(modifier = Modifier
            .clickable {
                onClick()
            }) {
            HorizontalDivider(Modifier.padding(top = 4.dp))
            ParagraphSubtitle(text = stringResource(Res.string.excesos))

            Row(verticalAlignment = Alignment.CenterVertically) {
                var texto = ""
                if (cuDetalle.excesos != null && cuDetalle.excesos!! > 0) {
                    texto =
                        stringResource(Res.string.exceso_de_jornada) + " " + cuDetalle.excesos?.toLocalTime() + " "
                }
                if (cuDetalle.mermas != null && cuDetalle.mermas!! > 0) {
                    texto += stringResource(Res.string.mermas) + " " + cuDetalle.mermas?.toLocalTime()
                }
                MyTextStd(text = texto)
                Icon(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "",
                    tint = azulKirito
                )
            }
        }
    }
}