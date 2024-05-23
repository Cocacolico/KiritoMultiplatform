package es.kirito.kirito.core.domain.util

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate


fun Long?.toLocalDate(): LocalDate {
    var salida = this
    if (salida == null)
        salida = 0
    return LocalDate.fromEpochDays(salida.toInt())
}


/** Devuelve el valor sin traducir, para usar en el back. */
fun DayOfWeek.inicial(): String {
    return when(this.ordinal){
        1-> "L"
        2-> "M"
        3-> "X"
        4-> "J"
        5-> "V"
        6-> "S"
        7-> "D"
        else -> ""
    }
}