package es.kirito.kirito.menu.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.kirito.kirito.core.data.dataStore.preferenciasKirito
import es.kirito.kirito.core.data.database.LsUsers
import es.kirito.kirito.core.domain.CoreRepository
import es.kirito.kirito.menu.data.network.models.RequestChangePassword
import es.kirito.kirito.menu.data.network.models.RequestEditarMiUsuario
import es.kirito.kirito.menu.domain.MenuRepository
import es.kirito.kirito.menu.domain.ProfileState
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.contrase_a_modificada_correctamente
import kirito.composeapp.generated.resources.datos_actualizados_correctamente
import kirito.composeapp.generated.resources.introduce_tu_contrase_a_anterior
import kirito.composeapp.generated.resources.la_contrase_a_antigua_no_coincide
import kirito.composeapp.generated.resources.la_contrasena_debe_tener_5_caracteres
import kirito.composeapp.generated.resources.las_contrasenas_no_coinciden
import kirito.composeapp.generated.resources.no_he_podido_procesar_tu_solicutud
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModel: ViewModel(), KoinComponent {

    private val coreRepo: CoreRepository by inject()
    private val repository: MenuRepository by inject()
    private val preferences = preferenciasKirito

    private val userID = preferences.map { it.userId }
    val residencia = preferences.map { it.residenciaName }

    val miUsuario = userID.flatMapLatest { id ->
        coreRepo.getMyUser(id)
    }

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    val toastString = MutableSharedFlow<String?>()
    val toastId = MutableSharedFlow<StringResource?>()

    fun onNameChange(name: String) {
        _state.update {
            it.copy(
                name = name
            )
        }
    }

    fun onSurnameChange(surname: String) {
        _state.update {
            it.copy(
                surname = surname
            )
        }
    }

    fun onWorkPhoneChange(phone: String) {
        _state.update {
            it.copy(
                workPhone = phone
            )
        }
    }

    fun onWorkPhoneExtChange(phone: String) {
        _state.update {
            it.copy(
                workPhoneExt = phone
            )
        }
    }

    fun onPersonalPhoneChange(phone: String) {
        _state.update {
            it.copy(
                personalPhone = phone
            )
        }
    }

    fun onEmailChange(email: String) {
        _state.update {
            it.copy(
                email = email
            )
        }
    }

    fun onModifcarDatosClick(userData: LsUsers) {
        _state.update {
            it.copy(
                id = userData.id,
                username = userData.username,
                name = userData.name,
                surname = userData.surname,
                email = userData.email,
                workPhone = userData.workPhone,
                workPhoneExt = userData.workPhoneExt,
                personalPhone = userData.personalPhone,
                showModificarDatosDialog = true,
                showCambiarPasswordDialog = false
            )
        }
    }

    fun onCambiarPasswordClick() {
        _state.update {
            it.copy(
                showCambiarPasswordDialog = true
            )
        }
    }

    fun onModificarDatosDialogDismiss() {
        _state.update {
            it.copy(
                showModificarDatosDialog = false
            )
        }
    }

    fun onCambiarPasswordDialogDismiss() {
        _state.update {
            it.copy(
                showCambiarPasswordDialog = false
            )
        }
    }
    fun onModificarDatosConfirm() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateMyUserData(_state.value.asUsuarioParaEditar())
                toastId.emit(Res.string.datos_actualizados_correctamente)
                _state.update {
                    it.copy(showModificarDatosDialog = false )
                }
            } catch (e: Exception) {
                toastId.emit(Res.string.no_he_podido_procesar_tu_solicutud)
            }
        }
    }

    fun onCambiarPasswordConfirm() {
        viewModelScope.launch(Dispatchers.IO) {
            with(_state.value) {
                if(oldPassword.length < 5) {
                    toastId.emit(Res.string.introduce_tu_contrase_a_anterior)
                } else if (newPassword.length < 5) {
                    toastId.emit(Res.string.la_contrasena_debe_tener_5_caracteres)
                } else if(newPassword != checkNewPassword) {
                    toastId.emit(Res.string.las_contrasenas_no_coinciden)
                } else {
                    try {
                        val salida = RequestChangePassword(
                            peticion = "usuarios.actualizar_password",
                            old_password = oldPassword,
                            new_password = newPassword
                        )
                        repository.changePassword(salida)
                        toastId.emit(Res.string.contrase_a_modificada_correctamente)
                        _state.update {
                            it.copy(showCambiarPasswordDialog = false)
                        }
                    } catch (e: Exception) {
                        when (e.message) {
                            "17" -> toastId.emit(Res.string.la_contrase_a_antigua_no_coincide)
                            "18" -> toastId.emit(Res.string.la_contrasena_debe_tener_5_caracteres)
                            else -> toastId.emit(Res.string.no_he_podido_procesar_tu_solicutud)
                        }
                    }
                }
            }
        }
    }
    fun clearToasts() {
        viewModelScope.launch {
            toastId.emit(null)
            toastString.emit(null)
        }
    }

    fun onOldPasswordChange(value: String) {
        _state.update {
            it.copy(
                oldPassword = value
            )
        }
    }

    fun onNewPasswordChange(value: String) {
        _state.update {
            it.copy(
                newPassword = value
            )
        }
    }

    fun onCheckPasswordChange(value: String) {
        _state.update {
            it.copy(
                checkNewPassword = value
            )
        }
    }
}

private fun ProfileState.asUsuarioParaEditar(): RequestEditarMiUsuario {
    return RequestEditarMiUsuario(
        peticion = "usuarios.actualizar",
        id = id.toString(),
        email = email,
        surname = surname,
        name = name,
        work_phone = workPhone,
        work_phone_ext = workPhoneExt,
        personal_phone = personalPhone,
        cambios_activados = null,
        mostrar_cuadros = null,
        mostrar_telf_trabajo = null,
        mostrar_telf_personal = null,
        recibir_email_notificaciones = null
    )
}