package kirito.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.kirito.kirito.login.data.network.ResidenciaDTO
import kirito.login.domain.LoginRepository
import kirito.login.domain.LoginState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    //Lo importante de un viewmodel para serlo, es que herede de ViewModel().
    //Aquí dentro ya tenemos todas las cosas chulas, como viewmodelscope.

    private val repository = LoginRepository()

    val state = MutableStateFlow(LoginState())

    private var clicksModoDev: Int = 0


    fun expandirResidencias(){
        state.update {
            it.copy(expanded = true)
        }
    }

    fun ocluirResidencias(){
        state.update {
            it.copy(expanded = false)
        }
    }


    fun testCorrutino() {
        viewModelScope.launch {
            //Aquí dentro van cosas en corrutinas.
        }
    }

    fun seleccionarResidencia(residencia: String) {
        state.update {
            it.copy (
                residenciaSeleccionada = residencia,
                expanded = false
            )
        }
    }

    fun activarModoDev() {
        clicksModoDev++
        if(clicksModoDev in 10..13)
            state.update {
                it.copy(
                    modoDevActivado = true
                )
            }
        else
            state.update {
                it.copy(
                    modoDevActivado = false
                )
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
                state.update {
                    it.copy(residencias = lista)
                }
            }
        }
    }
}