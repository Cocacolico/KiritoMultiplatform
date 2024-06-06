package es.kirito.kirito.core.presentation.components.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.button_aceptar
import kirito.composeapp.generated.resources.cancelar
import org.jetbrains.compose.resources.stringResource


/**
 * Tiene un botón de confirmar y otro de cancelar.
 * @param okText Para cambiar el texto de confirmar.
 * **/
@Composable
fun MyDialogConfirmation(
    show: Boolean,
    title: String,
    text: String,
    okText: String = stringResource(Res.string.button_aceptar),
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (show)
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text(okText)
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(stringResource(Res.string.cancelar))
                }
            },
            title = { Text(text = title) },
            text = {
                Text(text = text)
            }
        )
}