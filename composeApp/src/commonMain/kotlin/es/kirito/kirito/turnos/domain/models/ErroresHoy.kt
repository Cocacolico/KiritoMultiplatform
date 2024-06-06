package es.kirito.kirito.turnos.domain.models

data class ErroresHoy(
    val hayErrores: Boolean = false,
    val noHayCuDetalle: Boolean = false,
    val noHayGraficoEnVigor: Boolean = false,
    val noEstaEnElDiaQueToca: Boolean = false,
    val elTurnoEsUnTipo: Boolean = false,
    val elTurnoNoTieneTareas: Boolean = false,
    val elTurnoNoEstaEnElGrafico: Boolean = false,
)