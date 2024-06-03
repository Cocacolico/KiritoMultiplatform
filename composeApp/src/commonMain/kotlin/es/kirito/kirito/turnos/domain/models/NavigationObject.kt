package es.kirito.kirito.turnos.domain.models

data class NavigationObject(
    val destination: NavigationDestination?,
    val date: Long,
    val idGrafico: Long = -1,
    val diaSemana: String = "",
    val tren: String = "",
)