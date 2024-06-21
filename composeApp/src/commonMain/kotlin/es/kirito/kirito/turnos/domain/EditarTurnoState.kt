package es.kirito.kirito.turnos.domain

import es.kirito.kirito.core.data.database.CuDetalle
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

data class EditarTurnoState(
    val selectedDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val showDiasDebe: Boolean = false,
    val selectedShift: CuDetalle = CuDetalle(),
    val editedShift: CuDetalle = CuDetalle(),
    val usuariosEnNombreDebe: List<String> = emptyList(),
    val doneEditting: Boolean = false,
    val textTurno: String = "",
    val textNombreDebe: String = "",
)
