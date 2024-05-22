package es.kirito.kirito.precarga.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.kirito.kirito.precarga.domain.PrecargaRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import org.koin.core.component.KoinComponent


class PrecargaViewModel: ViewModel(), KoinComponent {

    private val repository: PrecargaRepository by inject()

    private val _timer = MutableStateFlow(0)
    val timer = _timer.asStateFlow()

    init {
        timerSiguienteScreen()
    }
    private fun timerSiguienteScreen() {
        viewModelScope.launch {
            while (_timer.value < 5) {
                delay(1000)
                _timer.value++
            }
        }
    }
}