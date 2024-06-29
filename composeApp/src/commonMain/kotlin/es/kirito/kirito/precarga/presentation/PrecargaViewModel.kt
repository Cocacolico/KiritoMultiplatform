package es.kirito.kirito.precarga.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.kirito.kirito.precarga.domain.PrecargaRepository
import es.kirito.kirito.precarga.domain.models.PreloadStep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import org.koin.core.component.KoinComponent


class PrecargaViewModel : ViewModel(), KoinComponent {

    private val repository: PrecargaRepository by inject()


    private val _state = MutableStateFlow(PrecargaState())
    private val pasosCompletados = repository.pasosCompletados

    val state = _state.asStateFlow().combine(pasosCompletados){ precargaState: PrecargaState, pasos: PreloadStep ->
        precargaState.copy(elementBeingUpdated = pasos)
    }




    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                println("Actualizando la base de datos")
                repository.updateKiritoDatabase()
            } catch (e: Exception) {
                println("Ha habido un error ${e.printStackTrace()}")
                _state.update {
                    it.copy(error = e.message)
                }
                println(e.message)
            }

        }
    }
}