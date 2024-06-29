package es.kirito.kirito.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.kirito.kirito.core.data.dataStore.preferenciasKirito
import es.kirito.kirito.core.data.dataStore.updatePreferenciasKirito
import es.kirito.kirito.login.data.network.ResidenciaDTO
import es.kirito.kirito.login.domain.LoginRepository
import es.kirito.kirito.login.domain.LoginState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel : ViewModel(), KoinComponent {

    private val repository: LoginRepository by inject()

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private var clicksModoDev: Int = 0

    val yaLogueado = preferenciasKirito.map { it.token.isNotBlank() }


    fun expandirResidencias() {
        _state.update {
            it.copy(expanded = true)
        }
    }

    fun ocluirResidencias() {
        _state.update {
            it.copy(expanded = false)
        }
    }


    fun seleccionarResidencia(residencia: ResidenciaDTO) {
        _state.update {
            it.copy(
                residenciaSeleccionada = residencia.nombre,
                urlResidenciaSeleccionada = residencia.directorio,
                expanded = false
            )
        }
    }

    fun activarModoDev() {
        clicksModoDev++
        when (clicksModoDev) {
            in 10..<13 -> _state.update {
                it.copy(
                    modoDevActivado = true
                )
            }

            13 -> clicksModoDev = 0
            else ->
                _state.update {
                    it.copy(
                        modoDevActivado = false
                    )
                }
        }
    }

    fun onValueUsuarioChange(value: String) {
        _state.update {
            it.copy(
                usuario = value
            )
        }
    }

    fun onValuePasswordChange(value: String) {
        _state.update {
            it.copy(
                password = value
            )
        }
    }

    fun onEntrarClick() {
        with(_state.value) {
            if (usuario.isBlank() || password.isBlank())
                _state.update {
                    it.copy(
                        errorCampoUserPassword = true
                    )
                }
            else {
                val nombreDispositivo = "Multiplatform pruebas"
                if (_state.value.modoDevActivado) { // Comprobamos si se ha activado el modo Developer para ajustar consecuentemente la URL
                    _state.update {
                        it.copy(
                            urlResidenciaSeleccionada = "chasca"
                        )
                    }
                }
                userLogin(nombreDispositivo)
            }

        }

    }


    private fun userLogin(
        nombreDispositivo: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //Primero guardo la residencia que ha seleccionado el usuario:
                updatePreferenciasKirito { settings ->
                    settings.copy(
                        residenciaName = _state.value.residenciaSeleccionada ?: "",
                        residenciaURL = _state.value.urlResidenciaSeleccionada ?: ""
                    )
                }

                //Ahora intento conseguir el token.
                repository.getMyKiritoToken(
                    residenciaUrl = _state.value.urlResidenciaSeleccionada,
                    usuario = _state.value.usuario,
                    password = _state.value.password,
                    nombreDispositivo = nombreDispositivo,
                    tokenFCM = getRandomString(24) // Hasta que podamos implementar Firebase genera un token de 24 caracteres aleatorios
                )
            } catch (e: Exception) {
                //TODO: Los errores habrá que mostrarlos, idealmente con un toast, este no es una excepción (jejje)
                println(e.message)
            }


        }
    }

    /**
     * Devuelve un string aleatorio de la longitud indicada.
     */
    private fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun descargarResidencias(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (_state.value.residencias.isEmpty())
                    repository.getResidencias().respuesta?.residencias?.let { lista ->
                        _state.update {
                            it.copy(residencias = lista)
                        }
                    }
            } catch (e: Exception) {
                //TODO: Mostrar un toast sobre el error.
            }
        }
    }


    init {

    }
}