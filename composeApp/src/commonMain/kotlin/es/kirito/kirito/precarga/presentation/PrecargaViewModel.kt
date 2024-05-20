package es.kirito.kirito.precarga.presentation

import androidx.lifecycle.ViewModel
import es.kirito.kirito.precarga.domain.PrecargaRepository
import org.koin.core.component.inject
import org.koin.core.component.KoinComponent


class PrecargaViewModel: ViewModel(), KoinComponent {

    private val repository: PrecargaRepository by inject()


}