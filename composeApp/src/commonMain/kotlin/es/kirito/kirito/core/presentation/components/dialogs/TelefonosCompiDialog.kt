package es.kirito.kirito.core.presentation.components.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.kirito.kirito.core.data.database.LsUsers
import es.kirito.kirito.core.domain.util.isNotNullNorBlank
import es.kirito.kirito.core.presentation.components.TextClicable
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.cerrar
import kirito.composeapp.generated.resources.exterior
import kirito.composeapp.generated.resources.ic_whatsapp
import kirito.composeapp.generated.resources.interior
import kirito.composeapp.generated.resources.personal
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun TelefonosCompiDialog(
    modifier: Modifier = Modifier,
    compi: LsUsers?,
    onCallClick: (String) -> Unit,
    onWhatsappClick: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(onDismissRequest = { onDismiss() },
        modifier = modifier,
        text = {
            Column {
                Text(
                    text = (compi?.name ?: "") + " " + (compi?.surname ?: ""),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource( Res.string.interior),
                        modifier = Modifier.weight(1f)
                    )
                    if (compi?.workPhone.isNotNullNorBlank() && compi?.mostrarTelfTrabajo == "1")
                        TextClicable(
                            text = compi.workPhone ,
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 4.dp)
                                .weight(2f)
                        ) { onCallClick(compi.workPhone) }
                }
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(Res.string.exterior),
                        modifier = Modifier.weight(1f)
                    )
                    if (compi?.workPhoneExt.isNotNullNorBlank() && compi?.mostrarTelfTrabajo == "1")
                        TextClicable(
                            text = compi.workPhoneExt ?: "",
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 4.dp)
                                .weight(2f)
                        ) { onCallClick(compi.workPhoneExt) }
                }
                Spacer(
                    modifier = Modifier.padding(
                        vertical = if (compi?.mostrarTelfPersonal == "1" && compi.workPhone.isNotNullNorBlank()) 2.dp else 8.dp
                    )
                )
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(Res.string.personal),
                        modifier = Modifier.weight(1f)
                    )
                    if (compi?.workPhone.isNotNullNorBlank() && compi?.mostrarTelfPersonal == "1") {
                        TextClicable(
                            text = compi.personalPhone ?: "",
                            modifier = Modifier
                                .padding(vertical = 4.dp, horizontal = 4.dp)
                                .weight(1f)
                        ) { onCallClick(compi.personalPhone) }
                        Image(painter = painterResource(Res.drawable.ic_whatsapp),
                            contentDescription = "",
                            Modifier
                                .weight(1f)
                                .clickable { onWhatsappClick(compi.personalPhone) })
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = stringResource(Res.string.cerrar))
            }
        })

}
