package es.kirito.kirito.menu.presentation

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.kirito.kirito.core.data.dataStore.preferenciasKirito
import es.kirito.kirito.core.data.database.LsUsers
import es.kirito.kirito.core.domain.CoreRepository
import es.kirito.kirito.menu.data.network.models.RequestEditarMiUsuario
import es.kirito.kirito.menu.domain.MenuRepository
import es.kirito.kirito.menu.domain.MenuState
import es.kirito.kirito.menu.domain.ProfileState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
            } catch (e: Exception) {

            }
        }
    }

    fun onCambiarPasswordConfirm() {
        TODO("Not yet implemented")
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