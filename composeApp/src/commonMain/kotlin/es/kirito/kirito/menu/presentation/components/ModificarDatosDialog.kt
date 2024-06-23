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
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.Icon
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
import es.kirito.kirito.core.presentation.components.MyTextSubTitle
import es.kirito.kirito.core.presentation.components.TitleText
import es.kirito.kirito.menu.domain.ProfileState
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.apellidos
import kirito.composeapp.generated.resources.cancelar
import kirito.composeapp.generated.resources.email
import kirito.composeapp.generated.resources.guardar_cambios
import kirito.composeapp.generated.resources.matricula
import kirito.composeapp.generated.resources.nombre
import kirito.composeapp.generated.resources.tel_fono_exterior_largo
import kirito.composeapp.generated.resources.tel_fono_interior_corto
import kirito.composeapp.generated.resources.tel_fono_personal
import kirito.composeapp.generated.resources.tus_datos
import org.jetbrains.compose.resources.stringResource

@Composable
fun ModificarDatosDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onNameChange: (String) -> Unit,
    onSurnameChange: (String) -> Unit,
    onWorkPhoneChange: (String) -> Unit,
    onWorkPhoneExtChange: (String) -> Unit,
    onPersonalPhoneChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
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
                .height(710.dp)
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(20.dp)
            ) {
                TitleText(titulo = stringResource(Res.string.tus_datos))
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Icon(Icons.Default.Train, "")
                    OutlinedTextField(
                        value = state.username,
                        readOnly = true,
                        onValueChange = { },
                        label = { Text(text = stringResource(Res.string.matricula)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .padding(4.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Icon(Icons.Default.Person, "")
                    Column(
                        modifier = Modifier
                            .padding(4.dp)
                    ) {
                        OutlinedTextField(
                            value = state.name,
                            onValueChange = { onNameChange(it) },
                            label = { Text(text = stringResource(Res.string.nombre)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        )
                        OutlinedTextField(
                            value = state.surname,
                            onValueChange = { onSurnameChange(it) },
                            label = { Text(text = stringResource(Res.string.apellidos)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Icon(Icons.Default.Phone, "")
                    Column(
                        modifier = Modifier
                            .padding(4.dp)
                    ) {
                        OutlinedTextField(
                            value = state.workPhone,
                            onValueChange = { onWorkPhoneChange(it) },
                            label = { Text(text = stringResource(Res.string.tel_fono_interior_corto)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                        OutlinedTextField(
                            value = state.workPhoneExt,
                            onValueChange = { onWorkPhoneExtChange(it) },
                            label = { Text(text = stringResource(Res.string.tel_fono_exterior_largo)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                        OutlinedTextField(
                            value = state.personalPhone,
                            onValueChange = { onPersonalPhoneChange(it) },
                            label = { Text(text = stringResource(Res.string.tel_fono_personal)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Icon(Icons.Default.Mail, "")
                    OutlinedTextField(
                        value = state.email,
                        onValueChange = { onEmailChange(it) },
                        label = { Text(text = stringResource(Res.string.email)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .padding(4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    OutlinedButton(
                        onClick = { onConfirm() },
                    ) {
                        Text(stringResource(Res.string.guardar_cambios))
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