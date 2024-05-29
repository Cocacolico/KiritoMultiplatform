package es.kirito.kirito.core.domain.util

import es.kirito.kirito.core.data.kiritoComponents.turnosContables
import es.kirito.kirito.core.data.kiritoComponents.turnosKirito
import es.kirito.kirito.core.domain.models.GrTarea
import es.kirito.kirito.core.domain.models.TurnoPrxTr

fun TurnoPrxTr.deAyerAHoy(inicioHoy: Long?): Long {
    if (inicioHoy == null || this.horaFin == null)
        return -1
    return if (this.pasaPorMedianoche()) {//El mismo día ambos.
        inicioHoy - this.horaFin!!
    } else {
        (86400 - this.horaFin!!) + inicioHoy

    }
}

fun TurnoPrxTr.deMananaAHoy(finHoy: Long?, despuesDeMN: Boolean): Long {
    if (finHoy == null || this.horaOrigen == null)
        return -1
    return if (despuesDeMN) { //mismo día ambos.
        this.horaOrigen!! - finHoy
    } else {
        86400 - finHoy + this.horaOrigen!!
    }
}

fun TurnoPrxTr.pasaPorMedianoche(): Boolean {
    if (this.horaOrigen != null && this.horaFin != null) {
        if (this.horaOrigen!! > this.horaFin!!)
            return true
    }
    return false
}

fun TurnoPrxTr.esTurnoDeTrabajo(): Boolean {
    return when (this.tipo) {
        "T", "TAD", "TC", "7000" -> true
        "DD" -> this.turno != "DD"
        else -> false
    }
}

/**Los turnos sobre los que hay que advertir*/
fun TurnoPrxTr.esTurnoContable(): Boolean {
    return when (this.tipo) {
        in turnosContables -> true
        else -> false
    }
}

fun String.esTipoValido(): Boolean {
    return turnosKirito.contains(this)
}

fun GrTarea.servicio(): String{
    if (this.observaciones != null && this.observaciones?.length != 0) {
        if (this.servicio == "PS") {
            return this.observaciones.toString()
        }
    }
    return this.servicio
}