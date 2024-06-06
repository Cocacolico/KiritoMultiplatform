package es.kirito.kirito.core.domain.models

data class TurnoBuscador(
    var turno: String,
    var numeroTurno: String,
    var idGrafico: Long?,
    var sitioOrigen: String?,
    var horaOrigen: Long?,
    var sitioFin: String?,
    var horaFin: Long?,
    var diaSemana: String?,
    var equivalencia: String?,
    var nombreCompi: String? = null,
    var idCompi: Long? = null,
    var nota: CharSequence? = null,
)