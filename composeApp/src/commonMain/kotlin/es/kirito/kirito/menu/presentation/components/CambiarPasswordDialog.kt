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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import es.kirito.kirito.core.presentation.components.TitleText
import es.kirito.kirito.menu.domain.ProfileState
import es.kirito.kirito.menu.domain.models.CambiarPasswordSteps
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.cambiar_contrase_a
import kirito.composeapp.generated.resources.cancelar
import kirito.composeapp.generated.resources.coinciden_las_dos_contrasennas
import kirito.composeapp.generated.resources.contrase_a_vieja
import kirito.composeapp.generated.resources.guardar
import kirito.composeapp.generated.resources.has_introducido_la_contrasenna_antigua
import kirito.composeapp.generated.resources.nueva_contrase_a
import kirito.composeapp.generated.resources.repite_la_contrase_a
import kirito.composeapp.generated.resources.tiene_6_caracteres_o_mas
import kirito.composeapp.generated.resources.tiene_un_numero
import kirito.composeapp.generated.resources.tiene_una_mayuscula
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
    var oldPasswordVacio by remember { mutableStateOf(false) }
    var passwordTiene5Caracteres by remember { mutableStateOf(false) }
    var passwordTieneMayuscula by remember { mutableStateOf(false) }
    var passwordTieneNumero by remember { mutableStateOf(false) }
    var passwordsCoinciden by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(5),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .height(560.dp)
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
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(2.dp)
                        ) {
                            if(state.oldPassword.length > 5) {
                                Icon(Icons.Default.CheckCircle,"", tint = Color.Green)
                                oldPasswordVacio = false
                            } else {
                                Icon(Icons.Default.Error,"", tint = Color.Red)
                                oldPasswordVacio = true
                            }
                            Text(
                                text = stringResource(Res.string.has_introducido_la_contrasenna_antigua),
                                color = if(oldPasswordVacio) Color.Red else Color.Green
                            )
                        }
                        Row (
                            modifier = Modifier
                                .padding(2.dp)
                        ){
                            if(state.newPassword.length > 5) {
                                Icon(Icons.Default.CheckCircle,"", tint = Color.Green)
                                passwordTiene5Caracteres = true
                            } else {
                                Icon(Icons.Default.Error,"", tint = Color.Red)
                                passwordTiene5Caracteres = false
                            }
                            Text(
                                text = stringResource(Res.string.tiene_6_caracteres_o_mas),
                                color = if(passwordTiene5Caracteres) Color.Green else Color.Red
                            )
                        }
                        Row (
                            modifier = Modifier
                                .padding(2.dp)
                        ){
                            if(state.newPassword.matches("^(?=.*[A-Z]).{4,}$".toRegex())) {
                                Icon(Icons.Default.CheckCircle,"", tint = Color.Green)
                                passwordTieneMayuscula = true
                            } else {
                                Icon(Icons.Default.Error,"", tint = Color.Red)
                                passwordTieneMayuscula = false
                            }

                            Text(text = stringResource(Res.string.tiene_una_mayuscula), color = if(passwordTieneMayuscula) Color.Green else Color.Red)
                        }
                        Row (
                            modifier = Modifier
                                .padding(2.dp)
                        ){
                            if(state.newPassword.matches("^(?=.*[0-9]).{4,}$".toRegex())) {
                                Icon(Icons.Default.CheckCircle,"", tint = Color.Green)
                                passwordTieneNumero = true
                            } else {
                                Icon(Icons.Default.Error,"", tint = Color.Red)
                                passwordTieneNumero = false
                            }
                            Text(text = stringResource(Res.string.tiene_un_numero), color = if(passwordTieneNumero) Color.Green else Color.Red)
                        }
                        Row (
                            modifier = Modifier
                                .padding(2.dp)
                        ){
                            if(state.checkNewPassword == state.newPassword && state.checkNewPassword.isNotEmpty()) {
                                Icon(Icons.Default.CheckCircle,"", tint = Color.Green)
                                passwordsCoinciden = true
                            } else {
                                Icon(Icons.Default.Error,"", tint = Color.Red)
                                passwordsCoinciden = false
                            }
                            Text(text = stringResource(Res.string.coinciden_las_dos_contrasennas), color = if(passwordsCoinciden) Color.Green else Color.Red)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { onConfirm() },
                        enabled = if(!oldPasswordVacio && passwordTiene5Caracteres && passwordTieneNumero && passwordTieneMayuscula && passwordsCoinciden) true else false
                    ) {
                        if(state.cambiarPasswordStep == CambiarPasswordSteps.SIN_MODIFICAR)
                            Text(stringResource(Res.string.guardar))
                        else if(state.cambiarPasswordStep == CambiarPasswordSteps.CAMBIANDO)
                            CircularProgressIndicator()
                        else if(state.cambiarPasswordStep == CambiarPasswordSteps.PASSWORD_OK)
                            Icon(Icons.Default.Check,"", tint = Color.Green)
                        else if(state.cambiarPasswordStep == CambiarPasswordSteps.PASSWORD_ERROR)
                            Icon(Icons.Default.Error,"", tint = Color.Red)
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