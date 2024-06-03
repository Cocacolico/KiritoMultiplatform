package es.kirito.kirito.turnos.domain.models

import kotlinx.datetime.LocalDate

data class CuDetalleConFestivoSemanal (
    var idDetalle: Long,
    var fecha: LocalDate,
    var tipo: String,
    var turno: String,
    var nombreDebe: String,
    var notas: String,
    var idFestivo: Long?,
    var descripcionFestivo: String?,
    var libra: Int,
    var comj: Int,
    var horaInicio: Long? = null,
    var horaFin: Long? = null,
    var color: Long? = null,
    var mermas: Int?,
    var excesos: Int?
)