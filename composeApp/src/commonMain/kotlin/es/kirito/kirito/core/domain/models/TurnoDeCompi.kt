package es.kirito.kirito.core.domain.models

data class TurnoDeCompi(
    var turno: String,
    var horaOrigen: Long?,
    var sitioOrigen: String?,
    var horaFin: Long?,
    var sitioFin: String?,
    var idGrafico: Long?,
    var desc_grafico: String?,
    var equivalencia: String?
)