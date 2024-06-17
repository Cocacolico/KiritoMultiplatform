package es.kirito.kirito.menu.presentation

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import es.kirito.kirito.core.data.dataStore.preferenciasKirito
import es.kirito.kirito.core.data.database.LsUsers
import es.kirito.kirito.core.domain.CoreRepository
import es.kirito.kirito.menu.domain.MenuRepository
import es.kirito.kirito.menu.domain.MenuState
import es.kirito.kirito.menu.domain.ProfileState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModel: ViewModel(), KoinComponent {

    private val coreRepo: CoreRepository by inject()
    private val repository: MenuRepository by inject()
    private val preferences = preferenciasKirito

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val userID = preferences.map { it.userId }
    private val residencia = preferences.map { it.residenciaName }

    val miUsuario = userID.flatMapLatest { id ->
        coreRepo.getMyUser(id)
    }

    fun onModificarDatosConfirm() {
        TODO("Not yet implemented")
    }

    fun onCambiarPasswordConfirm() {
        TODO("Not yet implemented")
    }

    fun onUsernameChange() {
        TODO("Not yet implemented")
    }

    fun onNameChange() {
        TODO("Not yet implemented")
    }

    fun onSurnameChange() {
        TODO("Not yet implemented")
    }

    fun onWorkPhoneChange() {
        TODO("Not yet implemented")
    }

    fun onWorkPhoneExtChange() {
        TODO("Not yet implemented")
    }

    fun onPersonalPhoneChange() {
        TODO("Not yet implemented")
    }

    fun onEmailChange() {
        TODO("Not yet implemented")
    }

    fun onModificarDatosClick(datosUsuario: LsUsers) {
        _state.value = ProfileState(
            userID = userID,
            residencia = residencia,
            userData = datosUsuario,
            showModificarDatosDialog = true,
            showCambiarPasswordDialog = false
        )
    }
}