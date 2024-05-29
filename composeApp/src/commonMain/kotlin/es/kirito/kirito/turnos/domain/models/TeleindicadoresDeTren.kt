package es.kirito.kirito.turnos.domain.models


data class TeleindicadoresDeTren(
    val tren: String,
    val teleindicadores: List<String>,
    var stringLength: Int = 6,
)