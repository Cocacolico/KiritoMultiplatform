package es.kirito.kirito.turnos.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.EditCalendar
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import es.kirito.kirito.core.presentation.components.MyTextStd
import es.kirito.kirito.core.presentation.theme.customColors
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.agregar_exceso_de_jornada
import kirito.composeapp.generated.resources.cambiar_con_un_compa_ero
import kirito.composeapp.generated.resources.editar_turno
import kirito.composeapp.generated.resources.editar_varios_turnos
import kirito.composeapp.generated.resources.ic_cambio
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun DialogEditShift(
    hasShiftsShared: Boolean = false,
    onDismiss: () -> Unit,
    onBulkEditClick: () -> Unit,
    onExcessClick: () -> Unit,
    onExchangeClick: () -> Unit,
    onEditClick: () -> Unit
) {

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
                .pointerInput(Unit){
                    detectTapGestures(
                        onTap = {
                            onDismiss()
                        }
                    )
                },
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(end = 16.dp, bottom = 72.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(bottom = 8.dp, end = 4.dp)
                        .clickable { onBulkEditClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyTextStd(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource( Res.string.editar_varios_turnos),
                        color = Color.White
                    )
                    SmallFloatingActionButton(onClick = { onBulkEditClick() },
                        containerColor = MaterialTheme.customColors.smallFABContainer,
                        contentColor = Color.Black) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                           imageVector = Icons.Outlined.EditCalendar,
                            contentDescription = ""
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(bottom = if (hasShiftsShared) 8.dp else 12.dp, end = 4.dp)
                        .clickable { onExcessClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyTextStd(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource( Res.string.agregar_exceso_de_jornada),
                        color = Color.White
                    )
                    SmallFloatingActionButton(onClick = { onExcessClick() },
                        containerColor = MaterialTheme.customColors.smallFABContainer,
                        contentColor = Color.Black) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Outlined.Schedule,
                            contentDescription = ""
                        )
                    }
                }
                if (hasShiftsShared)
                    Row(
                        modifier = Modifier
                            .padding(bottom = 12.dp, end = 4.dp)
                            .clickable { onExchangeClick() },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MyTextStd(
                            modifier = Modifier.padding(16.dp),
                            text = stringResource( Res.string.cambiar_con_un_compa_ero),
                            color = Color.White
                        )
                        SmallFloatingActionButton(onClick = { onExchangeClick() },
                            containerColor = MaterialTheme.customColors.smallFABContainer,
                            contentColor = Color.Black) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource( Res.drawable.ic_cambio),
                                contentDescription = ""
                            )
                        }
                    }

                Row(
                    modifier = Modifier.clickable { onEditClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyTextStd(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource( Res.string.editar_turno),
                        color = Color.White
                    )
                    FloatingActionButton(onClick = { onEditClick() },
                        contentColor = Color.Black,
                        containerColor = MaterialTheme.colorScheme.primary) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    }
}