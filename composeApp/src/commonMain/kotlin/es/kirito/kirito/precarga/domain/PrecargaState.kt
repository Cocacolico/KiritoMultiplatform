package es.kirito.kirito.precarga.domain

data class PrecargaState(
    val showScreen: Boolean = false, //Será true cuando no podamos actualizar en segundo plano.
    val navigateToNextScreen: Boolean = false,
    val elementBeingUpdated: String? = null
)