@file:OptIn(FormatStringsInDatetimeFormats::class)

package es.kirito.kirito.core.domain.util

import androidx.compose.runtime.Composable
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.full_days
import kirito.composeapp.generated.resources.full_months
import kirito.composeapp.generated.resources.short_months
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atDate
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.format.char
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringArrayResource

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
    val enMinutos = this / 60
    val hora = enMinutos / 60
    val minute = enMinutos % 60
    return LocalTime(hora, minute)
}
fun Long.toLocalTime(): LocalTime{
    return this.toInt().toLocalTime()
}

fun LocalDateTime.toEpochSecondsZoned(): Long {
   return this.toInstant(TimeZone.currentSystemDefault()).epochSeconds
}
fun LocalDateTime.toEpochSeconds(): Long {
   return this.toInstant(TimeZone.UTC).epochSeconds
}

fun Instant.toLocalTime(): LocalTime {
   return this.toLocalDateTime(TimeZone.UTC)
        .time
}

fun LocalTime.toStringUpToSeconds(): String {
    val format = LocalTime.Formats.ISO
    return LocalTime(this.hour, this.minute,this.second).format(format)
}

fun Instant.toLocalDate(): LocalDate {
   return this.toLocalDateTime(TimeZone.UTC)
        .date
}

fun Instant.toMinuteOfDay(): Int {
    return this.toLocalTime().toSecondOfDay() / 60
}
fun LocalTime.toMinuteOfDay(): Int {
    return this.toSecondOfDay() / 60
}

fun LocalTime.minusHours(hours: Int): LocalTime {
    var substracted = this.toSecondOfDay() - hours * 3600
    if (substracted < 0)
        substracted += 24 * 3600
    return substracted.toLocalTime()
}

fun Instant.isBefore(other: Instant): Boolean {
    return this < other
}

fun Long.restar24(): Long {
    return this % 24
}

/** Devuelve una String con varios ceros por delante, por defecto 2.
 * @param leadingZeros Número de ceros por delante. **/
fun Int.withLeadingZeros(leadingZeros: Int = 2): String{
    var output = this.toString()
    while (output.length < leadingZeros){
        output = "0$output"
    }
    return output
}

@Composable
fun Month.enCastellano(): String {
    return stringArrayResource(Res.array.full_months)[this.ordinal]
}

@Composable
fun DayOfWeek.enCastellano(): String {
    return stringArrayResource(Res.array.full_days)[this.ordinal]
}


/** Formato 12/31/2022**/
fun LocalDate.enMiFormato(): String {
    return this.dayOfMonth.toString() + "/" + (this.month.ordinal + 1).toString() + "/" + this.year.toString()
}

@Composable
fun LocalDate.enMiFormatoMedio(): String {
    return this.dayOfMonth.toString() + "-" + this.month.aMesCorto() + "-" + this.year.toString()
}

@Composable
private fun Month.aMesCorto(): String {
    //IMPORTANTE!!!!: Le resto 1 porque si no, muestra el mes siguiente.
    return stringArrayResource(Res.array.short_months)[this.ordinal - 1]
}

fun LocalDate.withDayOfMonth(dayOfMonth: Int): LocalDate {
    return LocalDate(this.year, this.month, dayOfMonth)
}

fun LocalDate.lastDayOfMonth(): LocalDate {
    return this.plus(1, DateTimeUnit.MONTH)
        .minus(3,DateTimeUnit.DAY)
}

/** Se usa sobre una hora de origen y se le resta la hora de fin. */
fun Long.minusEndTime(endTime: Long): Long {
    return if (this < endTime)
        endTime - this
    else
        86400 - this + endTime
}

fun String?.deBinarioASemanal(): CharSequence {
    if(this == null)
        return ""
    val letterMapping = mapOf(
        0 to 'L',
        1 to 'M',
        2 to 'X',
        3 to 'J',
        4 to 'V',
        5 to 'S',
        6 to 'D',
        7 to 'F'
    )

    return this.mapIndexed { index, binaryChar ->
        if (binaryChar == '1') letterMapping[index] ?: error("Invalid binary digit: $binaryChar")
        else '_'
    }.joinToString("")
}

fun diasBooleansToString(l: Boolean, m: Boolean, x: Boolean, j: Boolean, v: Boolean, s: Boolean, d: Boolean, f: Boolean): String {
    var salida: String = ""
    salida += if (l) "L" else "_"
    salida += if (m) "M" else "_"
    salida += if (x) "X" else "_"
    salida += if (j) "J" else "_"
    salida += if (v) "V" else "_"
    salida += if (s) "S" else "_"
    salida += if (d) "D" else "_"
    salida += if (f) "F" else "_"
    return salida
}


fun Int.toInicialSemana(): String {
    return when (this) {
        0 -> "L"
        1 -> "M"
        2 -> "X"
        3 -> "J"
        4 -> "V"
        5 -> "S"
        6 -> "D"
        7 -> "F"
        else -> "todo"
    }
}

fun String.fromInicialSemana(): Int {
    return when (this) {
        "L" -> 0
        "M" -> 1
        "X" -> 2
        "J" -> 3
        "V" -> 4
        "S" -> 5
        "D" -> 6
        "F" -> 7
        else -> 0
    }
}
















