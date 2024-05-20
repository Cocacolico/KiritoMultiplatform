package es.kirito.kirito.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.kirito.kirito.core.data.dataStore.updatePreferenciasKirito
import es.kirito.kirito.login.domain.LoginRepository
import es.kirito.kirito.login.domain.LoginState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginViewModel: ViewModel(), KoinComponent {

    private val repository: LoginRepository by inject()

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private var clicksModoDev: Int = 0


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


    fun seleccionarResidencia(residencia: String) {
        _state.update {
            it.copy(
                residenciaSeleccionada = residencia,
                expanded = false
            )
        }
    }
    fun activarTest(){
        clicksModoDev = 12
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
            if(usuario.isBlank() || password.isBlank())
                _state.update {
                    it.copy(
                        errorCampoUserPassword = true
                    )
                }
            else {
                val nombreDispositivo = "Multiplatform pruebas"
                userLogin(nombreDispositivo)
            }

        }
        
    }
    // Función para relacionar la residencia seleccionada por el usuario con el directorio.
    // De cara a que en el futuro se hagan las peticiones a una residencia u otra.
    private fun obtenerDirectorioResidencia(): String? {
        var directorio: String? = null
        _state.value.residencias.find {
            it.nombre == _state.value.residenciaSeleccionada
        }?.let {
            directorio = it.directorio
        }
        return directorio
    }

    private fun userLogin(
        nombreDispositivo: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val kiritoToken = repository.getMyKiritoToken(
                usuario = _state.value.usuario,
                password = _state.value.password,
                nombreDispositivo = nombreDispositivo,
                tokenFCM = getRandomString(24) // Hasta que podamos implementar Firebase genera un token de 24 caracteres aleatorios
            )
            if(kiritoToken.respuesta?.respuesta?.login?.token != "null"){ //Tenemos un login correcto, vamos a probar la BBDD
                kiritoToken.respuesta?.respuesta?.login?.let {
                    println("Login correcto")
                    // Actualizamos DataStore
                    updatePreferenciasKirito {appSettings ->
                        appSettings.copy(
                            matricula = _state.value.usuario,
                            userId = it.id_usuario.toLongOrNull() ?: -2L,
                            token = it.token
                        )
                    }

                }
            } else
                println("Error con el login")
        }
    }

    /**
     * Devuelve un string aleatorio de la longitud indicada.
     */
    private fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    val estaciones = repository.estaciones
    fun onDescargarEstacionesClick() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.refreshEstaciones()
            println("Metiendo estaciones!")
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getResidencias().respuesta?.residencias?.let { lista ->
                _state.update {
                    it.copy(residencias = lista)
                }
            }
        }
    }
}