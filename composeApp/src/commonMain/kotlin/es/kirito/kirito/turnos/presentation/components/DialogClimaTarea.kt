package es.kirito.kirito.turnos.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import es.kirito.kirito.core.domain.models.GrTarea
import es.kirito.kirito.core.domain.util.servicio
import es.kirito.kirito.core.domain.util.textoServicio
import es.kirito.kirito.core.domain.util.toLocalTime
import es.kirito.kirito.core.presentation.components.MyTextStd
import es.kirito.kirito.core.presentation.components.ParagraphSubtitle
import es.kirito.kirito.core.presentation.components.ParagraphTitle
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.__mm_de_lluvia
import kirito.composeapp.generated.resources._espacio_
import kirito.composeapp.generated.resources.celsius
import kirito.composeapp.generated.resources.cerrar
import kirito.composeapp.generated.resources.prob_lluvia
import kirito.composeapp.generated.resources.prob_nieve
import kirito.composeapp.generated.resources.ver_clave_en_el_buscador
import kirito.composeapp.generated.resources.xx_cm_de_nieve
import org.jetbrains.compose.resources.stringResource


@Composable
fun DialogClimaTarea(tarea: GrTarea, onDismiss: () -> Unit, onSearch: (String) -> Unit) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
        ) {

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                ParagraphTitle(text = tarea.textoServicio(), modifier = Modifier)
                if (tarea.lluviaO != null && tarea.nubladoO != null
                    && tarea.vientoO != null && tarea.nieveO != null
                    && tarea.probO != null && tarea.visibilidadO != null
                ) {
                    ParagraphSubtitle(
                        text = stringResource(
                            resource = Res.string._espacio_,
                            tarea.sitioOrigen.toString(),
                            tarea.horaOrigen?.toLocalTime().toString()
                        ), modifier = Modifier.padding(top = 8.dp)
                    )
                    Row (verticalAlignment = Alignment.CenterVertically) {
                        IconClima(
                            modifier = Modifier.padding(end = 8.dp),
                            mmLluvia = tarea.lluviaO!!,
                            cmNieve = tarea.nieveO!!,
                            probPrecip = tarea.probO!!,
                            visibilidad = tarea.visibilidadO!!,
                            viento = tarea.vientoO!!,
                            nublado = tarea.nubladoO!!
                        )
                        Column {
                            val mmLluvia = if (tarea.lluviaO != 0f) stringResource(
                                Res.string.__mm_de_lluvia,
                                tarea.lluviaO.toString()
                            ) else ""
                            val cmNieve = if (tarea.nieveO != 0F) stringResource(
                                Res.string.xx_cm_de_nieve,
                                tarea.nieveO.toString()
                            ) else ""

                            MyTextStd(
                                text = if (tarea.nieveO != 0f) "${
                                    stringResource(
                                         Res.string.prob_nieve,
                                        tarea.probO.toString()
                                    )
                                }%$cmNieve"
                                else "${
                                    stringResource(
                                        Res.string.prob_lluvia,
                                        tarea.probO.toString()
                                    )
                                }%$mmLluvia"
                            )
                            MyTextStd(
                                text = stringResource(
                                    Res.string.celsius,
                                    tarea.tempO.toString(),
                                    (tarea.vientoO ?: 0f).toString()
                                )
                            )
                        }
                    }
                }

                if (tarea.lluviaF != null && tarea.nubladoF != null
                    && tarea.vientoF != null && tarea.nieveF != null
                    && tarea.probF != null && tarea.visibilidadF != null
                ) {
                    ParagraphSubtitle(
                        text = stringResource(
                             Res.string._espacio_,
                            tarea.sitioFin.toString(),
                            tarea.horaFin?.toLocalTime().toString()
                        ), modifier = Modifier.padding(top = 8.dp)
                    )
                    Row (verticalAlignment = Alignment.CenterVertically) {
                        IconClima(
                            modifier = Modifier.padding(end = 8.dp),
                            mmLluvia = tarea.lluviaF!!,
                            cmNieve = tarea.nieveF!!,
                            probPrecip = tarea.probF!!,
                            visibilidad = tarea.visibilidadF!!,
                            viento = tarea.vientoF!!,
                            nublado = tarea.nubladoF!!
                        )
                        Column {
                            val mmLluvia = if (tarea.lluviaF != 0f) stringResource(
                                Res.string.__mm_de_lluvia,
                                tarea.lluviaF.toString()
                            ) else ""
                            val cmNieve = if (tarea.nieveF != 0F) stringResource(
                                Res.string.xx_cm_de_nieve,
                                tarea.nieveF.toString()
                            ) else ""

                            MyTextStd(
                                text = if (tarea.nieveF != 0f) "${
                                    stringResource(
                                        Res.string.prob_nieve,
                                        tarea.probF.toString()
                                    )
                                }%$cmNieve"
                                else "${
                                    stringResource(
                                        Res.string.prob_lluvia,
                                        tarea.probF.toString()
                                    )
                                }%$mmLluvia"
                            )
                            MyTextStd(
                                text = stringResource(
                                    Res.string.celsius,
                                    tarea.tempF.toString(),
                                    (tarea.vientoF ?: 0f).toString()
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(vertical = 4.dp))

                Column(
                    Modifier
                        .align(Alignment.End)
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalAlignment = Alignment.End) {
                    TextButton(onClick = { onSearch(tarea.servicio) }) {
                        Text(
                            text = stringResource(
                                 Res.string.ver_clave_en_el_buscador,
                                tarea.servicio()
                            )
                        )
                    }
                    TextButton(onClick = { onDismiss() }) {
                        Text(text = stringResource( Res.string.cerrar))
                    }
                }
            }
        }
    }
}



