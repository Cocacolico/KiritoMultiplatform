package es.kirito.kirito.turnos.domain.models

import kotlinx.datetime.LocalDate

data class CuDetalleConFestivoSemanal (
    val idDetalle: Long,
    val fecha: LocalDate,
    val tipo: String,
    val turno: String,
    val nombreDebe: String,
    val notas: String,
    val idFestivo: Long?,
    val descripcionFestivo: String?,
    val libra: Int,
    val comj: Int,
    val horaInicio: Long? = null,
    val horaFin: Long? = null,
    val color: Long? = null,
    val mermas: Int?,
    val excesos: Int?,
    val isSelected: Boolean = false,
)