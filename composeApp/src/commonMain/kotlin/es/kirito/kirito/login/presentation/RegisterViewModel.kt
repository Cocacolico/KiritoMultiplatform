package es.kirito.kirito.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.kirito.kirito.login.domain.LoginRepository
import es.kirito.kirito.login.domain.RegisterState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: LoginRepository
) : ViewModel() {


    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()
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



    fun onValueNombreChange(value: String) {
        _state.update {
            it.copy(
                nombre = value
            )
        }
    }

    fun onValueApellidosChange(value: String) {
        _state.update {
            it.copy(
                apellidos = value
            )
        }
    }

    fun onValueTelefonoCortoChange(value: String) {
        _state.update {
            it.copy(
                telefonoIntCorto = value
            )
        }
    }

    fun onValueTelefonoLargoChange(value: String) {
        _state.update {
            it.copy(
                telefonoIntLargo = value
            )
        }
    }

    fun onVisibilidadTelefonoEmpresaChanged(value: Boolean) {
        _state.update {
            it.copy(
                visibilidadTelefonoEmpresa = value
            )
        }
    }

    fun onValueTelefonoPersonalChange(value: String) {
        _state.update {
            it.copy(
                telefonoPersonal = value
            )
        }
    }

    fun onVisibilidadTelefonoPersonalChanged(value: Boolean) {
        _state.update {
            it.copy(
                visibilidadTelefonoPersonal = value
            )
        }
    }

    fun onValueEmailChange(value: String) {
        _state.update {
            it.copy(
                email = value
            )
        }
    }

    fun onValuePasswordCheckChange(value: String) {
        _state.update {
            it.copy(
                passwordCheck = value
            )
        }
    }

    fun onValueComentariosChange(value: String) {
        _state.update {
            it.copy(
                comentarios = value
            )
        }
    }
    fun onClickButtonEnviar() {
        with(_state.value) {
            if(residenciaSeleccionada.isBlank())
                _state.update {
                    it.copy (
                        errorResidenciaVacio = true
                    )
                }
            else if(usuario.length != 7)
                _state.update {
                    it.copy (
                        errorUsuarioErroneo = true
                    )
                }
            else if(password.length < 5)
                _state.update {
                    it.copy (
                        errorPasswordNoCumpleLongitud = true
                    )
                }
            else if(passwordCheck != password)
                _state.update {
                    it.copy (
                        errorPasswordNoCoincide = true
                    )
                }
            else
                TODO("Enviar la petición a Jesús y tal")
        }
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
