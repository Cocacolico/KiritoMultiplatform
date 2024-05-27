package es.kirito.kirito.precarga.presentation

import es.kirito.kirito.precarga.domain.models.PreloadStep

data class PrecargaState(
    val showScreen: Boolean = false, //Será true cuando no podamos actualizar en segundo plano.
    val navigateToNextScreen: Boolean = false,
    val elementBeingUpdated: PreloadStep = PreloadStep.BEGINNING,
    val error: String? = null,
)