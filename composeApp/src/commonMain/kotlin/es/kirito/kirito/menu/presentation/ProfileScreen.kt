@file:OptIn(KoinExperimentalAPI::class)

package es.kirito.kirito.menu.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import es.kirito.kirito.core.data.database.LsUsers
import es.kirito.kirito.core.domain.util.enMiFormato
import es.kirito.kirito.core.domain.util.toLocalDate
import es.kirito.kirito.core.presentation.components.MyTextStd
import es.kirito.kirito.core.presentation.components.MyTextSubTitle
import es.kirito.kirito.core.presentation.components.TextClicable
import es.kirito.kirito.core.presentation.components.TitleText
import es.kirito.kirito.menu.domain.MenuState
import es.kirito.kirito.menu.domain.ProfileState
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.apellidos
import kirito.composeapp.generated.resources.cambiar_contrase_a
import kirito.composeapp.generated.resources.cancelar
import kirito.composeapp.generated.resources.contrase_a_vieja
import kirito.composeapp.generated.resources.email
import kirito.composeapp.generated.resources.enviar
import kirito.composeapp.generated.resources.eres_administrador
import kirito.composeapp.generated.resources.estas_en_residencia
import kirito.composeapp.generated.resources.fecha_de_alta__
import kirito.composeapp.generated.resources.guardar
import kirito.composeapp.generated.resources.guardar_cambios
import kirito.composeapp.generated.resources.matricula
import kirito.composeapp.generated.resources.matricula_
import kirito.composeapp.generated.resources.mi_perfil
import kirito.composeapp.generated.resources.modificar_mis_datos
import kirito.composeapp.generated.resources.nombre
import kirito.composeapp.generated.resources.nueva_contrase_a
import kirito.composeapp.generated.resources.repite_la_contrase_a
import kirito.composeapp.generated.resources.tel_fono_exterior_largo
import kirito.composeapp.generated.resources.tel_fono_interior_corto
import kirito.composeapp.generated.resources.tel_fono_personal
import kirito.composeapp.generated.resources.telefono_interior
import kirito.composeapp.generated.resources.tus_datos
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
fun ProfileScreen() {
    val viewModel = koinViewModel<ProfileViewModel>()
    val state by viewModel.state.collectAsState(ProfileState())
    val miUsuario by viewModel.miUsuario.collectAsState(LsUsers())

    var showModificarDatosDialog by remember { mutableStateOf(false) }
    var showCambiarPasswordDialog by remember { mutableStateOf(false) }

    Surface(
        Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
            ) {
                TitleText(titulo = stringResource(Res.string.mi_perfil))

                HorizontalDivider(thickness = 2.dp)
                Column (
                    modifier = Modifier
                        .padding(16.dp)
                ){
                    MyTextStd(
                        text = stringResource(Res.string.matricula_,miUsuario.username)
                    )
                    if(state.userData.created != null)
                        MyTextStd(
                            text = stringResource(Res.string.fecha_de_alta__,miUsuario.created.toLocalDate().enMiFormato())
                        )
                    if(state.userData.admin == "1")
                        MyTextStd(
                            text = stringResource(Res.string.eres_administrador)
                        )
                    MyTextStd(
                        text = stringResource(Res.string.estas_en_residencia,state.residencia)
                    )
                }
                HorizontalDivider(thickness = 2.dp)
                TextClicable(
                    text = stringResource(Res.string.modificar_mis_datos),
                    onClick = { viewModel.onModificarDatosClick(miUsuario) },
                    modifier = Modifier
                        .padding(16.dp)
                )
                HorizontalDivider(thickness = 2.dp)
                TextClicable(
                    text = stringResource(Res.string.cambiar_contrase_a),
                    onClick = { showCambiarPasswordDialog = true },
                    modifier = Modifier
                        .padding(16.dp)
                )
                HorizontalDivider(thickness = 2.dp)
            if(showModificarDatosDialog)
                ModificarDatosDialog(
                    onDismiss = { showModificarDatosDialog = false },
                    onConfirm = {
                        showModificarDatosDialog = false
                        viewModel.onModificarDatosConfirm()
                    },
                    viewModel = viewModel
                )
            if(showCambiarPasswordDialog)
                CambiarPasswordDialog(
                    onDismiss = { showCambiarPasswordDialog = false },
                    onConfirm = {
                        showCambiarPasswordDialog = false
                        viewModel.onCambiarPasswordConfirm()
                    },
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun ModificarDatosDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    viewModel: ProfileViewModel
) {
    val state by viewModel.state.collectAsState(ProfileState())

        Dialog(
            onDismissRequest = { onDismiss() }
        ){
            Column (
                modifier = Modifier
                    .fillMaxWidth()
            ){
                MyTextSubTitle(
                    text = stringResource(Res.string.tus_datos)
                )
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                Row {
                    Icon(Icons.Default.Train,"")
                    OutlinedTextField(
                        value = state.userData.username,
                        readOnly = true,
                        onValueChange = { viewModel.onUsernameChange() },
                        label = { Text(text = stringResource(Res.string.matricula)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                }
                Row {
                    Icon(Icons.Default.Person,"")
                    Column(
                    modifier = Modifier
                        .padding(4.dp)
                    ) {
                        OutlinedTextField(
                            value = state.userData.name,
                            onValueChange = { viewModel.onNameChange() },
                            label = { Text(text = stringResource(Res.string.nombre)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        )
                        OutlinedTextField(
                            value = state.userData.surname,
                            onValueChange = { viewModel.onSurnameChange() },
                            label = { Text(text = stringResource(Res.string.apellidos)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        )
                    }
                }
                Row {
                    Icon(Icons.Default.Phone,"")
                    Column (
                        modifier = Modifier
                            .padding(4.dp)
                    ){
                        OutlinedTextField(
                            value = state.userData.workPhone,
                            onValueChange = { viewModel.onWorkPhoneChange() },
                            label = { Text(text = stringResource(Res.string.tel_fono_interior_corto)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                        OutlinedTextField(
                            value = state.userData.workPhoneExt,
                            onValueChange = { viewModel.onWorkPhoneExtChange() },
                            label = { Text(text = stringResource(Res.string.tel_fono_exterior_largo)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                        OutlinedTextField(
                            value = state.userData.personalPhone,
                            onValueChange = { viewModel.onPersonalPhoneChange() },
                            label = { Text(text = stringResource(Res.string.tel_fono_personal)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                    }
                }
                Row {
                    Icon(Icons.Default.Mail,"")
                    OutlinedTextField(
                        value = state.userData.email,
                        onValueChange = { viewModel.onEmailChange() },
                        label = { Text(text = stringResource(Res.string.email)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    )
                }
                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    OutlinedButton(
                        onClick = { onConfirm() } ,
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
@Composable
fun CambiarPasswordDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    viewModel: ProfileViewModel
) {
    val state by viewModel.profileState.collectAsState(ProfileState())

    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
            Column {
                TitleText(stringResource(Res.string.cambiar_contrase_a))
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text(text = stringResource(Res.string.contrase_a_vieja)) }
                )
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text(text = stringResource(Res.string.nueva_contrase_a)) }
                )
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text(text = stringResource(Res.string.repite_la_contrase_a)) }
                )
                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    OutlinedButton(
                        onClick = { onConfirm() } ,
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