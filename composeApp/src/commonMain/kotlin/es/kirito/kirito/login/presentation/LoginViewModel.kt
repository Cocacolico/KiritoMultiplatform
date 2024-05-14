package es.kirito.kirito.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.kirito.kirito.core.data.dataStore.updatePreferenciasKirito
import es.kirito.kirito.core.data.database.KiritoDao
import es.kirito.kirito.core.data.database.KiritoDatabase
import es.kirito.kirito.login.domain.LoginRepository
import es.kirito.kirito.login.domain.LoginState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: LoginRepository,
): ViewModel() {
    //Lo importante de un viewmodel para serlo, es que herede de ViewModel().
    //Aquí dentro ya tenemos todas las cosas chulas, como viewmodelscope.



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

    private fun postLogin() {
        TODO("Descargar cuadro de tareas en BBDD")
    }
    private fun userLogin(
        nombreDispositivo: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val kiritoToken = repository.getMyKiritoToken(
                usuario = _state.value.usuario,
                password = _state.value.password,
                nombreDispositivo = nombreDispositivo,
                tokenFCM = getRandomString(20)
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

    private fun obtenerDirectorioResidencia(): String? {
        var directorio: String? = null
        _state.value.residencias.find {
            it.nombre == _state.value.residenciaSeleccionada
        }?.let {
            directorio = it.directorio
        }
        return directorio
    }

    fun onEntrarClick() {
        val nombreDispositivo = "Multiplatform pruebas"
        userLogin(nombreDispositivo)
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

    init {//Sí, los viewmodels tienen su método init{}, que se ejecuta al crearse el viewmodel.
        //Aquí puedes hacer cosas que se ejecutan al principio.
        viewModelScope.launch(Dispatchers.IO) { //A esta corrutina le he pedido
            //que cambie su hilo al de IO, no al que se usa para mostrar la UI, que es Main.

            //Nos las bajamos de internet.
            //Ojo, esto, cuando lo hagamos en serio, habrá que cazar excepciones y tal.
            repository.getResidencias().respuesta?.residencias?.let { lista ->

                //Importante esto también. Esta es la manera más "actual" que he visto en que se deberían
                //usar los estados de compose. Un único objeto y dentro de él todas las cosas. Se actualiza
                //así su valor y no de otra manera, pues así forzamos la recomposición. Si usas var en vez de val,
                //te fallarán algunas recomposiciones y no sabrás por qué.
                _state.update {
                    it.copy(residencias = lista)
                }
            }
        }
    }
}