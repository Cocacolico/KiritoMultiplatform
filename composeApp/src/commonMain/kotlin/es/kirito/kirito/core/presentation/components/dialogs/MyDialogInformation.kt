package es.kirito.kirito.core.presentation.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.button_aceptar
import org.jetbrains.compose.resources.stringResource



/**
 * Solo tiene un botón de confirmar.
 * @param okText Para cambiar el texto de confirmar.
 * @param isDismissable si true, se puede descartar pinchando fuera.
 * **/
@Composable
fun MyDialogInformation(
    show: Boolean,
    title: String? = null,
    text: String,
    okText: String = stringResource( Res.string.button_aceptar),
    isDismissable: Boolean = true,
    fontSize: TextUnit = 14.sp,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (show)
        if (title == null)
        AlertDialog(
            onDismissRequest = {
                if (isDismissable)
                    onDismiss()
            },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text(okText)
                }
            },
            text = {
                Text(text = text, fontSize = fontSize)
            }
        )
    else
        AlertDialog(
            onDismissRequest = {
                if (isDismissable)
                    onDismiss()
            },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text(okText)
                }
            },
            title = { Text(text = title) },
            text = {
                Text(text = text, fontSize = fontSize)
            }
        )
}

