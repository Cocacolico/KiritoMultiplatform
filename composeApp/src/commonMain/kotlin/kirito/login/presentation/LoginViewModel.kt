package kirito.login.presentation

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.kirito.kirito.login.data.network.ResidenciaDTO
import kirito.login.domain.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    //Lo importante de un viewmodel para serlo, es que herede de ViewModel().
    //Aquí dentro ya tenemos todas las cosas chulas, como viewmodelscope.

    private val repository = LoginRepository()


    val contador = mutableIntStateOf(2)

    val residencias = MutableStateFlow(emptyList<ResidenciaDTO>())

    fun updateContador() {
        contador.value += 1
    }

    fun testCorrutino() {
        viewModelScope.launch {
            //Aquí dentro van cosas en corrutinas.
        }
    }


    init {//Sí, los viewmodels tienen su método init{}, que se ejecuta al crearse el viewmodel.
        //Aquí puedes hacer cosas que se ejecutan al principio.
        viewModelScope.launch(Dispatchers.IO) { //A esta corrutina le he pedido
            //que cambie su hilo al de IO, no al que se usa para mostrar la UI, que es Main.

            //Nos las bajamos de internet.
            //Ojo, esto, cuando lo hagamos en serio, habrá que cazar excepciones y tal.
            repository.getResidencias().respuesta?.residencias?.let { lista ->
                residencias.value = lista
            }
        }
    }
}