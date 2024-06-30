@file:OptIn(KoinExperimentalAPI::class)

package es.kirito.kirito.menu.presentation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import es.kirito.kirito.core.data.database.LsUsers
import es.kirito.kirito.core.domain.util.enFormatoDeSalida
import es.kirito.kirito.core.domain.util.enMiFormato
import es.kirito.kirito.core.domain.util.toInstant
import es.kirito.kirito.core.domain.util.toLocalDate
import es.kirito.kirito.core.presentation.components.LongToast
import es.kirito.kirito.core.presentation.components.MyTextStd
import es.kirito.kirito.core.presentation.components.MyTextSubTitle
import es.kirito.kirito.core.presentation.components.TextClicable
import es.kirito.kirito.core.presentation.components.TitleText
import es.kirito.kirito.core.presentation.theme.KiritoColors
import es.kirito.kirito.core.presentation.theme.KiritoTheme
import es.kirito.kirito.menu.domain.MenuState
import es.kirito.kirito.menu.domain.ProfileState
import es.kirito.kirito.menu.presentation.components.CambiarPasswordDialog
import es.kirito.kirito.menu.presentation.components.ModificarDatosDialog
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources._0
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
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
fun ProfileScreen() {
    val viewModel = koinViewModel<ProfileViewModel>()
    val miUsuario by viewModel.miUsuario.collectAsState(LsUsers())
    val miResidencia by viewModel.residencia.collectAsState(String)
    val state by viewModel.state.collectAsState(ProfileState())

    val toastString by viewModel.toastString.collectAsState(null)
    val toastId by viewModel.toastId.collectAsState(null)

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
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    MyTextStd(
                        text = stringResource(Res.string.matricula_, miUsuario.username)
                    )
                    if (miUsuario.created != null)
                        MyTextStd(
                            text = stringResource(
                                Res.string.fecha_de_alta__,
                                miUsuario.created.toInstant().toLocalDate().enMiFormato()
                            )
                        )
                    if (miUsuario.admin == "1")
                        MyTextStd(
                            text = stringResource(Res.string.eres_administrador)
                        )
                    MyTextStd(
                        text = stringResource(Res.string.estas_en_residencia, miResidencia)
                    )
                }
                HorizontalDivider(thickness = 2.dp)
                TextClicable(
                    text = stringResource(Res.string.modificar_mis_datos),
                    onClick = { viewModel.onModifcarDatosClick(miUsuario) },
                    modifier = Modifier
                        .padding(16.dp)
                )
                HorizontalDivider(thickness = 2.dp)
                TextClicable(
                    text = stringResource(Res.string.cambiar_contrase_a),
                    onClick = { viewModel.onCambiarPasswordClick() },
                    modifier = Modifier
                        .padding(16.dp)
                )
                HorizontalDivider(thickness = 2.dp)
                if (state.showModificarDatosDialog)
                    ModificarDatosDialog(
                        onDismiss = { viewModel.onModificarDatosDialogDismiss() },
                        onConfirm = { viewModel.onModificarDatosConfirm() },
                        onNameChange = { viewModel.onNameChange(it) },
                        onSurnameChange = { viewModel.onSurnameChange(it) },
                        onWorkPhoneChange = { viewModel.onWorkPhoneChange(it) },
                        onWorkPhoneExtChange = { viewModel.onWorkPhoneExtChange(it) },
                        onPersonalPhoneChange = { viewModel.onPersonalPhoneChange(it) },
                        onEmailChange = { viewModel.onEmailChange(it) },
                        state = state
                    )
                if (state.showCambiarPasswordDialog)
                    CambiarPasswordDialog(
                        onDismiss = { viewModel.onCambiarPasswordDialogDismiss() },
                        onConfirm = { viewModel.onCambiarPasswordConfirm() },
                        onOldPasswordChange = { viewModel.onOldPasswordChange(it) },
                        onNewPasswordChange = { viewModel.onNewPasswordChange(it) },
                        onCheckPasswordChange = { viewModel.onCheckPasswordChange(it) },
                        state = state
                    )
            }
            if (toastString != null) {
                LongToast(toastString)
                viewModel.clearToasts()
            }
            if (toastId != null) {
                LongToast(stringResource(toastId ?: Res.string._0))
                viewModel.clearToasts()
            }
        }
    }
}