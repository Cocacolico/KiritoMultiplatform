package es.kirito.kirito.menu.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import es.kirito.kirito.core.presentation.components.TitleText
import es.kirito.kirito.menu.domain.ProfileState
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.cambiar_contrase_a
import kirito.composeapp.generated.resources.cancelar
import kirito.composeapp.generated.resources.contrase_a_vieja
import kirito.composeapp.generated.resources.guardar
import kirito.composeapp.generated.resources.nueva_contrase_a
import kirito.composeapp.generated.resources.repite_la_contrase_a
import org.jetbrains.compose.resources.stringResource

@Composable
fun CambiarPasswordDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onOldPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onCheckPasswordChange: (String) -> Unit,
    state: ProfileState
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(5),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .height(420.dp)
                .padding(20.dp)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                TitleText(stringResource(Res.string.cambiar_contrase_a))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .weight(1f)
                ) {
                    OutlinedTextField(
                        value = state.oldPassword,
                        onValueChange = { onOldPasswordChange(it) },
                        label = { Text(text = stringResource(Res.string.contrase_a_vieja)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.newPassword,
                        onValueChange = { onNewPasswordChange(it) },
                        label = { Text(text = stringResource(Res.string.nueva_contrase_a)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    OutlinedTextField(
                        value = state.checkNewPassword,
                        onValueChange = { onCheckPasswordChange(it) },
                        label = { Text(text = stringResource(Res.string.repite_la_contrase_a)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { onConfirm() },
                    ) {
                        Text(stringResource(Res.string.guardar))
                    }
                    OutlinedButton(
                        onClick = { onDismiss() }
                    ) {
                        Text(stringResource(Res.string.cancelar))
                    }
                }
            }
        }
    }
}