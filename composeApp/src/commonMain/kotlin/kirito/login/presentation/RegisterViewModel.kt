package kirito.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kirito.login.domain.LoginRepository
import kirito.login.domain.LoginState
import kirito.login.domain.RegisterState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    fun expandirResidencias() {
        TODO("Not yet implemented")
    }

    fun ocluirResidencias() {
        TODO("Not yet implemented")
    }

    fun seleccionarResidencia(nombre: String) {

    }

    fun onValueUsuarioChange(value: String) {

    }

    fun onValuePasswordChange(value: String) {

    }

    fun onClickButtonEnviar() {
        TODO("Not yet implemented")
    }

    fun onValueNombreChange(value: String) {

    }

    fun onValueApellidosChange(value: String) {

    }

    fun onValueTelefonoCortoChange(value: String) {

    }

    fun onValueTelefonoLargoChange(value: String) {

    }

    fun onVisibilidadTelefonoChanged(value: Boolean) {

    }

    fun onVisibilidadTelefonoPersonalChanged(value: Boolean) {

    }

    fun onValueEmailChange(value: Any) {

    }

    fun onValuePasswordCheckChange(value: String) {

    }

    fun onValueComentariosChange(value: String) {

    }

    private val repository = LoginRepository()

    val state = MutableStateFlow(RegisterState())

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
                state.update {
                    it.copy(residencias = lista)
                }
            }
        }
    }

}
