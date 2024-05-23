package es.kirito.kirito.core.domain.util

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char


fun Long?.toLocalDate(): LocalDate {
    var salida = this
    if (salida == null)
        salida = 0
    return LocalDate.fromEpochDays(salida.toInt())
}
fun Long?.toInstant(): Instant {
    var salida = this
    if (salida == null)
        salida = 0
    return Instant.fromEpochSeconds(salida)
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

/*** En formato "yyyy-MM-dd HH:mm:ss" que busca Jesús. */
fun Instant.enFormatoDeSalida(): String{
    val format = DateTimeComponents.Format {
        date(LocalDate.Formats.ISO)
        char(' ')
        hour()
        char(':')
        minute()
        char(':')
        second()
    }
    return this.format(format)
}

/**Formato yyyy-MM-dd
 * Para usar     Internet ---> BD**/
fun String?.fromDateStringToLong(): Long {

    var salida = this ?: "2000-01-01"
    if (this == "0000-00-00" || this == "null")
        salida = "2000-01-01"
    return LocalDate
        .parse(salida, LocalDate.Formats.ISO)
        .toEpochDays()
        .toLong()
}

