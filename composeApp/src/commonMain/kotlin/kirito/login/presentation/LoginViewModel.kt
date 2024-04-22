package kirito.login.presentation

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    //Lo importante de un viewmodel para serlo, es que herede de ViewModel().
    //Aquí dentro ya tenemos todas las cosas chulas, como viewmodelscope.



    val contador = mutableIntStateOf(2)


    fun updateContador() {
        contador.value += 1
    }

    fun testCorrutino(){
        viewModelScope.launch(Dispatchers.IO) {
            //Aquí dentro van cosas en el hilo de IO.
        }
    }

}