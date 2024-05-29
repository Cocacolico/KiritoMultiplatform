package es.kirito.kirito.core.domain.util

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant

internal val formatJesus = DateTimeComponents.Format {
    date(LocalDate.Formats.ISO)
    char(' ')
    hour()
    char(':')
    minute()
    char(':')
    second()
}

internal val formatStandard = DateTimeComponents.Format {
    date(LocalDate.Formats.ISO)
    char('T')
    hour()
    char(':')
    minute()
}

fun Long?.toLocalDate(): LocalDate {
    var salida = this
    if (salida == null)
        salida = 0
    return LocalDate.fromEpochDays(salida.toInt())
}

/** Equivalente a toLocalDateTime, por así decirlo. */
fun Long?.toInstant(): Instant {
    var salida = this
    if (salida == null)
        salida = 0
    return Instant.fromEpochSeconds(salida)
}


/** Devuelve el valor sin traducir, para usar en el back. */
fun DayOfWeek.inicial(): String {
    return when (this.ordinal) {
        1 -> "L"
        2 -> "M"
        3 -> "X"
        4 -> "J"
        5 -> "V"
        6 -> "S"
        7 -> "D"
        else -> ""
    }
}

/*** En formato "yyyy-MM-dd HH:mm:ss" que busca Jesús. Devuelve null si es 0. */
fun Instant.enFormatoDeSalida(): String? {
    if (this.epochSeconds == 0L)
        return null
    return this.format(formatJesus)
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

/** En formato yyyy-MM-dd HH:mm:ss
 * Para usar     Internet ---> BD **/
fun String?.fromDateTimeStringToLong(): Long? {
    return if (this == "0000-00-00 00:00:00" || this == "null" || this == null)
        null
    else
        Instant.parse(this, formatJesus).epochSeconds
}

/** En formato yyyy-MM-ddTHH:mm
 * Para usar     Internet ---> BD **/
fun String.fromDateTimeStdStringToInstant(): Instant {
    return Instant.parse(this, formatStandard)
}


/** Recibe a la entrada un string en formato 12:00:00 **/
fun String?.fromTimeStringToInt(): Int? {
    if (this == "00:00:00" || this == null)
        return null
    return LocalTime.parse(this).toSecondOfDay()
}


/** Recibe a la entrada un string en formato 12:00 **/
fun String?.fromTimeWOSecsStringToInt(): Int {
    val salida = this
    if (this == null || this == "--:--" || this == "null")
        return 0
    val parts = salida!!.split(":")
    if (parts.size != 2)
        return 0
    val hours = (parts[0].toIntOrNull() ?: 0).coerceAtMost(23)
    val minutes = (parts[1].toIntOrNull() ?: 0).coerceAtMost(59)

    return LocalTime(hours, minutes).toSecondOfDay()
}

fun Instant.roundUpToHour(): Instant {
    val minutes = this.epochSeconds / 60
    val onlyMinutes = minutes % 60
    val output = if (onlyMinutes < 30)
        minutes - onlyMinutes
    else
        minutes - onlyMinutes + 60
    return (output * 60).toInstant()
}

fun Int.toLocalTime(): LocalTime {
    val minute = this / 60
    val hora = minute / 60
    return LocalTime(hora, minute)
}
fun Long.toLocalTime(): LocalTime{
    return this.toInt().toLocalTime()
}

fun LocalDateTime.toEpochSeconds(): Long {
   return this.toInstant(TimeZone.currentSystemDefault()).epochSeconds
}




















