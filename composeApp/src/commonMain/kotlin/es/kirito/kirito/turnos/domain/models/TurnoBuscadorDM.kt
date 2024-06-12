package es.kirito.kirito.turnos.domain.models

import es.kirito.kirito.core.data.database.GrTareas
import es.kirito.kirito.core.data.database.OtColoresTrenes
import es.kirito.kirito.core.domain.models.GrTareaConClima

data class TurnoBuscadorDM(
    var turno: String,
    var numeroTurno: String,
    var idGrafico: Long?,
    var sitioOrigen: String?,
    var horaOrigen: Long?,
    var sitioFin: String?,
    var horaFin: Long?,
    var diaSemana: String?,
    var equivalencia: String?,
    var listaTareas: List<GrTareaConClima>?,
    var listaTareasCortas: List<GrTareas>?,
    var coloresTrenes: List<OtColoresTrenes>?,
    var nombreCompi: String?,
    var idCompi: Long?,
    val notas: CharSequence?,
)