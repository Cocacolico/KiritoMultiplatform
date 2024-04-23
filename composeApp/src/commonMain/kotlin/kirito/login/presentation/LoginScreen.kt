package kirito.login.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.contrase_a
import kirito.composeapp.generated.resources.entrar
import kirito.composeapp.generated.resources.es_tu_primera_vez_en_kirito
import kirito.composeapp.generated.resources.introduce_tu_usuario_y_contrase_a
import kirito.composeapp.generated.resources.matr_cula
import kirito.composeapp.generated.resources.modo_desarrollador_activado
import kirito.composeapp.generated.resources.olvid_mi_contrase_a
import kirito.composeapp.generated.resources.registrarme
import kirito.composeapp.generated.resources.selecciona_tu_residencia
import kirito.composeapp.generated.resources.ver_contrase_a
import kirito.core.presentation.components.MyTextStd

import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun LoginScreen() {
    val viewModel = LoginViewModel()//Con esta línea invocas al viewmodel.
    val state by viewModel.state.collectAsState()

    Surface(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {

                if(state.modoDevActivado){
                    MyTextStd(text = stringResource(Res.string.modo_desarrollador_activado))
                }
                ExposedDropdownMenuBox(
                    expanded = state.expanded,
                    onExpandedChange = {
                        viewModel.expandirResidencias()
                    }
                ) {
                    OutlinedTextField(
                        value = state.residenciaSeleccionada?: "",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.expanded)
                        },
                        placeholder = { MyTextStd(stringResource(Res.string.selecciona_tu_residencia)) },
                        modifier = Modifier.menuAnchor()
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
                TextButton(
                    onClick = { viewModel.activarModoDev() }
                ) {
                    MyTextStd(stringResource(Res.string.introduce_tu_usuario_y_contrase_a))
                }
                OutlinedTextField(
                    value = state.usuario,
                    onValueChange = {},
                    label = { Text(stringResource(Res.string.matr_cula)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                Row() {
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = {},
                        label = { Text(stringResource(Res.string.contrase_a)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true
                    )
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Warning,
                            contentDescription = stringResource(Res.string.ver_contrase_a)
                        )

                    }
                }
                Button(
                    onClick = { TODO("Hacer las acciones de login")}
                ) {
                    MyTextStd(stringResource(Res.string.entrar))
                }
                TextButton(
                    onClick = { TODO("Enlazar con screen para recuperar contraseña")}
                ) {
                    MyTextStd(
                        text = stringResource(Res.string.olvid_mi_contrase_a),
                        color = Color.Blue
                    )
                }
                Row {
                    MyTextStd(stringResource(Res.string.es_tu_primera_vez_en_kirito))
                    Button(
                        onClick = { TODO("Llevar a la screen de registro")}
                    ) {
                        MyTextStd(stringResource(Res.string.registrarme))
                    }
                }
            }
        }
    }
}