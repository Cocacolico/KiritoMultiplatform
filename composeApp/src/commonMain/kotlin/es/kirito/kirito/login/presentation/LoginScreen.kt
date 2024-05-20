package es.kirito.kirito.login.presentation

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import es.kirito.kirito.core.data.dataStore.preferenciasKirito
import es.kirito.kirito.core.data.dataStore.updatePreferenciasKirito
import es.kirito.kirito.core.data.database.KiritoDatabase
import es.kirito.kirito.core.presentation.components.MyIconError
import es.kirito.kirito.core.presentation.components.MyTextError
import es.kirito.kirito.core.presentation.components.MyTextStd
import es.kirito.kirito.login.domain.LoginRepository
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.contrase_a
import kirito.composeapp.generated.resources.entrar
import kirito.composeapp.generated.resources.es_tu_primera_vez_en_kirito
import kirito.composeapp.generated.resources.introduce_tu_usuario_y_contrase_a
import kirito.composeapp.generated.resources.matr_cula
import kirito.composeapp.generated.resources.modo_desarrollador_activado
import kirito.composeapp.generated.resources.ocultar
import kirito.composeapp.generated.resources.olvid_mi_contrase_a
import kirito.composeapp.generated.resources.registrarme
import kirito.composeapp.generated.resources.selecciona_tu_residencia
import kirito.composeapp.generated.resources.usuario_o_contrase_a_incorrectos
import kirito.composeapp.generated.resources.ver_contrase_a
import kotlinx.coroutines.flow.first
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    val viewModel = viewModel<LoginViewModel>{
        LoginViewModel(
            repository = LoginRepository()
        )
    }//Con esta línea invocas al viewmodel.
    val state by viewModel.state.collectAsState()

    var showPassword by remember { mutableStateOf(false) }

    Surface(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {


            if (state.modoDevActivado) {
                MyTextStd(text = stringResource(Res.string.modo_desarrollador_activado))
            }
            Column(Modifier.align(Alignment.CenterEnd).padding(horizontal = 16.dp).fillMaxWidth()) {
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

                Spacer(Modifier.padding(vertical = 12.dp))

                MyTextStd(stringResource(Res.string.introduce_tu_usuario_y_contrase_a),
                    Modifier.pointerInput(Unit) {
                        detectTapGestures(onTap = { viewModel.activarModoDev() })
                    })

                OutlinedTextField(
                    value = state.usuario,
                    onValueChange = { viewModel.onValueUsuarioChange(it) },
                    label = { Text(stringResource(Res.string.matr_cula)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = state.errorCampoUserPassword,
                    supportingText = {
                        if (state.errorCampoUserPassword) {
                            MyTextError(stringResource(Res.string.usuario_o_contrase_a_incorrectos))
                        }
                    },
                    singleLine = true,
                    trailingIcon = {
                        if (state.errorCampoUserPassword)
                            MyIconError()
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.password,
                    onValueChange = { viewModel.onValuePasswordChange(it) },
                    label = { Text(stringResource(Res.string.contrase_a)) },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        if (state.errorCampoUserPassword)
                            MyIconError()
                        else
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
                    isError = state.errorCampoUserPassword,
                    supportingText = {
                        if (state.errorCampoUserPassword) {
                            MyTextError(stringResource(Res.string.usuario_o_contrase_a_incorrectos))
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { viewModel.onEntrarClick() },
                    modifier = Modifier.align(alignment = Alignment.End)
                ) {
                    MyTextStd(stringResource(Res.string.entrar))
                }
                LaunchedEffect(Unit) {
                    //Con esto probamos el datastore.
                    var preferences = preferenciasKirito.first()
                    println("dark mode 0 is $preferences")
                    updatePreferenciasKirito {appSettings ->
                        appSettings.copy(estoyInicializado = true)
                    }
                    preferences = preferenciasKirito.first()
                    println("dark mode 1 is $preferences")

                    println("dark mode 2 is $preferences")
                }
                TextButton(
                    onClick = {
                        viewModel.onDescargarEstacionesClick()
                    },
                    modifier = Modifier.align(alignment = Alignment.End)
                ) {
                    MyTextStd(
                        text = "Descargar estaciones",
                    )
                }
                val estaciones by viewModel.estaciones.collectAsState(emptyList())
                LaunchedEffect(estaciones){
                    println("Las estaciones son $estaciones")
                }



                TextButton(
                    onClick = {
                        navController.navigate("recuperarPassword")
                    },
                    modifier = Modifier.align(alignment = Alignment.End)
                ) {
                    MyTextStd(
                        text = stringResource(Res.string.olvid_mi_contrase_a),
                    )
                }
            }

            Row(
                Modifier.align(Alignment.BottomEnd).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MyTextStd(
                    stringResource(Res.string.es_tu_primera_vez_en_kirito),
                    Modifier.padding(horizontal = 16.dp)
                )
                Button(
                    onClick = { navController.navigate("register") }
                ) {
                    MyTextStd(stringResource(Res.string.registrarme))
                }
            }

        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PasswordVisibilityToggleIcon(
    showPassword: Boolean,
    onTogglePasswordVisibility: () -> Unit
) {
    IconButton(onClick = onTogglePasswordVisibility) {
        Icon(
            imageVector = if (showPassword) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
            contentDescription = if (showPassword) Res.string.ocultar.toString() else Res.string.ver_contrase_a.toString()
        )
    }
}

