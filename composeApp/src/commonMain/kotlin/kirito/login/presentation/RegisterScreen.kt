package kirito.login.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Train
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kirito.composeapp.generated.resources.Res
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import kirito.composeapp.generated.resources.apellidos
import kirito.composeapp.generated.resources.comentario_al_admin
import kirito.composeapp.generated.resources.contrase_a
import kirito.composeapp.generated.resources.el_email_solo_lo_usaremos___
import kirito.composeapp.generated.resources.email
import kirito.composeapp.generated.resources.enviar
import kirito.composeapp.generated.resources.matr_cula
import kirito.composeapp.generated.resources.mis_compa_eros_pueden_ver_mi_tel_fono_personal
import kirito.composeapp.generated.resources.mis_compa_eros_pueden_ver_mis_tel_fonos_de_empresa
import kirito.composeapp.generated.resources.nombre
import kirito.composeapp.generated.resources.ocultar
import kirito.composeapp.generated.resources.puedes_poner_un_comentario_para_que_el_administrador_que_te_dar_de_alta_sepa_qui_n_eres
import kirito.composeapp.generated.resources.reg_strate
import kirito.composeapp.generated.resources.repite_la_contrase_a
import kirito.composeapp.generated.resources.selecciona_tu_residencia
import kirito.composeapp.generated.resources.tel_fono_exterior_largo
import kirito.composeapp.generated.resources.tel_fono_interior_corto
import kirito.composeapp.generated.resources.tel_fono_personal
import kirito.composeapp.generated.resources.telefono_exterior
import kirito.composeapp.generated.resources.telefono_interior
import kirito.composeapp.generated.resources.usuario_o_contrase_a_incorrectos
import kirito.composeapp.generated.resources.ver_contrase_a
import kirito.core.presentation.components.MyIconError
import kirito.core.presentation.components.MyTextError
import kirito.core.presentation.components.MyTextStd
import kirito.core.presentation.components.TitleText
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen() {
    val viewModel = RegisterViewModel()
    val state by viewModel.state.collectAsState()

    var showPassword by remember { mutableStateOf(false) }

    val errorUsuarioErroneo by remember {
        derivedStateOf {
            state.errorUsuarioErroneo
        }
    }
    val errorPasswordErroneo by remember {
        derivedStateOf {
            state.errorPasswordErroneo
        }
    }
    val errorPasswordNoCoincide by remember {
        derivedStateOf {
            state.errorPasswordNoCoincide
        }
    }

    Surface(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            TitleText(stringResource(Res.string.reg_strate))

            ExposedDropdownMenuBox(
                expanded = state.expanded,
                onExpandedChange = {
                    viewModel.expandirResidencias()
                }
            ) {
                OutlinedTextField(
                    value = state.residenciaSeleccionada ?: "",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.expanded)
                    },
                    placeholder = { MyTextStd(stringResource(Res.string.selecciona_tu_residencia)) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = state.expanded,
                    onDismissRequest = { viewModel.ocluirResidencias() }
                ) {
                    state.residencias.forEach { residencia ->
                        DropdownMenuItem(
                            text = { MyTextStd(residencia.nombre) },
                            onClick = {
                                viewModel.seleccionarResidencia(residencia.nombre)
                            }
                        )
                    }
                }
            }
            OutlinedTextField(
                value = state.usuario,
                onValueChange = { viewModel.onValueUsuarioChange(it) },
                label = { Text(stringResource(Res.string.matr_cula)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = errorUsuarioErroneo,
                supportingText = {
                    if (errorUsuarioErroneo) {
                        MyTextError(stringResource(Res.string.usuario_o_contrase_a_incorrectos))
                    }
                },
                singleLine = true,
                trailingIcon = {
                    if (errorUsuarioErroneo)
                        MyIconError()
                },
                leadingIcon = { Icon(Icons.Outlined.Train,"") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.nombre,
                onValueChange = { viewModel.onValueNombreChange(it) },
                label = { Text(stringResource(Res.string.nombre)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                leadingIcon = { Icon(Icons.Outlined.Person,"")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.apellidos,
                onValueChange = { viewModel.onValueApellidosChange(it) },
                label = { Text(stringResource(Res.string.apellidos)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.telefonoIntCorto,
                onValueChange = { viewModel.onValueTelefonoCortoChange(it) },
                label = { Text(stringResource(Res.string.tel_fono_interior_corto)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                leadingIcon = { Icon(Icons.Outlined.Call,"")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.telefonoIntLargo,
                onValueChange = { viewModel.onValueTelefonoLargoChange(it) },
                label = { Text(stringResource(Res.string.tel_fono_exterior_largo)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            MyTextStd(stringResource(Res.string.mis_compa_eros_pueden_ver_mis_tel_fonos_de_empresa))
            Checkbox(
                checked = state.visibilidadTelefonoEmpresa,
                onCheckedChange = { viewModel.onVisibilidadTelefonoChanged(it) }
            )
            OutlinedTextField(
                value = state.telefonoPersonal,
                onValueChange = { viewModel.onValueTelefonoLargoChange(it) },
                label = { Text(stringResource(Res.string.tel_fono_personal)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            MyTextStd(stringResource(Res.string.mis_compa_eros_pueden_ver_mi_tel_fono_personal))
            Checkbox(
                checked = state.visibilidadTelefonoPersonal,
                onCheckedChange = { viewModel.onVisibilidadTelefonoPersonalChanged(it) }
            )
            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.onValueEmailChange(it) },
                label = { Text(stringResource(Res.string.email)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                leadingIcon = { Icon(Icons.Outlined.Email, "")},
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            MyTextStd(stringResource(Res.string.el_email_solo_lo_usaremos___))

            OutlinedTextField(
                value = state.password,
                onValueChange = { viewModel.onValuePasswordChange(it) },
                label = { Text(stringResource(Res.string.contrase_a)) },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    if (errorPasswordErroneo)
                        MyIconError()
                    else
                    // Password visibility toggle icon
                        PasswordVisibilityToggleIcon(
                            showPassword = showPassword,
                            onTogglePasswordVisibility = {
                                showPassword = !showPassword
                            })
                },
                leadingIcon = { Icon(Icons.Outlined.LockOpen,"")},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                isError = errorPasswordErroneo,
                supportingText = {
                    if (errorPasswordErroneo) {
                        MyTextError("La contraseña no cumple con las condiciones")
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.passwordCheck,
                onValueChange = { viewModel.onValuePasswordCheckChange(it) },
                label = { Text(stringResource(Res.string.repite_la_contrase_a)) },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    if (errorPasswordNoCoincide)
                        MyIconError()
                    else
                    // Password visibility toggle icon
                        PasswordVisibilityToggleIcon(
                            showPassword = showPassword,
                            onTogglePasswordVisibility = {
                                showPassword = !showPassword
                            })
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                isError = errorPasswordNoCoincide,
                supportingText = {
                    if (errorPasswordNoCoincide) {
                        MyTextError("La contraseña no coincide con la introducida anteriormente")
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.comentarios,
                onValueChange = { viewModel.onValueComentariosChange(it) },
                label = { Text(stringResource(Res.string.comentario_al_admin)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = false,
                modifier = Modifier.fillMaxWidth()
            )
            MyTextStd(stringResource(Res.string.puedes_poner_un_comentario_para_que_el_administrador_que_te_dar_de_alta_sepa_qui_n_eres))

            Button(
                onClick = { viewModel.onClickButtonEnviar()}
            ) {
                MyTextStd(stringResource(Res.string.enviar))
            }
        }
}
}