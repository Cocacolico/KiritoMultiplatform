package es.kirito.kirito.precarga.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.kirito.kirito.precarga.domain.PrecargaRepository
import es.kirito.kirito.precarga.domain.PrecargaState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import org.koin.core.component.KoinComponent


class PrecargaViewModel : ViewModel(), KoinComponent {

    private val repository: PrecargaRepository by inject()

    private val _timer = MutableStateFlow(0)
    val timer = _timer.asStateFlow()


    private val _state = MutableStateFlow(PrecargaState())
    val state = _state.asStateFlow()

    val festivos = repository.festivos


    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateKiritoDatabase()
            } catch (e: Exception) {
                println(e.message)
            }

        }
    }
}