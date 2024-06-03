package es.kirito.kirito.core.domain.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import es.kirito.kirito.core.data.database.CuDetalle
import es.kirito.kirito.core.data.kiritoComponents.turnosCambiables
import es.kirito.kirito.core.data.kiritoComponents.turnosContables
import es.kirito.kirito.core.data.kiritoComponents.turnosKirito
import es.kirito.kirito.core.domain.models.CuDetalleConFestivoDBModel
import es.kirito.kirito.core.domain.models.GrTarea
import es.kirito.kirito.core.domain.models.TurnoPrxTr
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.cedido_cgo
import kirito.composeapp.generated.resources.compensacion_de_jornada
import kirito.composeapp.generated.resources.curso_de_formacion
import kirito.composeapp.generated.resources.descanso
import kirito.composeapp.generated.resources.descanso_al_debe
import kirito.composeapp.generated.resources.descanso_compensacion
import kirito.composeapp.generated.resources.descanso_compensado
import kirito.composeapp.generated.resources.descanso_detraible
import kirito.composeapp.generated.resources.descripcion_tareas_keys
import kirito.composeapp.generated.resources.descripcion_tareas_values
import kirito.composeapp.generated.resources.festivo_semanales
import kirito.composeapp.generated.resources.formacion_adaptativa
import kirito.composeapp.generated.resources.lic_cambio_domicilio
import kirito.composeapp.generated.resources.lic_deber_inexcusable
import kirito.composeapp.generated.resources.lic_examen_renfe
import kirito.composeapp.generated.resources.lic_fallecimiento_familiar
import kirito.composeapp.generated.resources.lic_sin_sueldo_asuntos_propios
import kirito.composeapp.generated.resources.no_encontrado
import kirito.composeapp.generated.resources.paso_maniobra_
import kirito.composeapp.generated.resources.rec_mantenimiento_titulo
import kirito.composeapp.generated.resources.reciclaje_habilitacion_material
import kirito.composeapp.generated.resources.tren_
import kirito.composeapp.generated.resources.turno
import kirito.composeapp.generated.resources.huelga_notificada
import kirito.composeapp.generated.resources.compensacion_dj
import kirito.composeapp.generated.resources.compensacion_dja
import kirito.composeapp.generated.resources.baja_enfermedad_comun
import kirito.composeapp.generated.resources.baja_accidente_laboral
import kirito.composeapp.generated.resources.turno_especial
import kirito.composeapp.generated.resources.accidente_trabajo_tren
import kirito.composeapp.generated.resources.reposo_domiciliario
import kirito.composeapp.generated.resources.funciones_sindicales
import kirito.composeapp.generated.resources.jornada_de_transicion
import kirito.composeapp.generated.resources.lic_enfermedad_familiar
import kirito.composeapp.generated.resources.lic_traslado_domicilio
import kirito.composeapp.generated.resources.lic_fallecimiento_familiar_no_directo
import kirito.composeapp.generated.resources.lic_nacimiento
import kirito.composeapp.generated.resources.lic_matrimonio
import kirito.composeapp.generated.resources.lic_examen_oficial
import kirito.composeapp.generated.resources.lic_movilidad_geografica
import kirito.composeapp.generated.resources.rec_mant_titulo_simulador
import kirito.composeapp.generated.resources.rec_medico
import kirito.composeapp.generated.resources.lic_sin_sueldo
import kirito.composeapp.generated.resources.lic_lactancia
import kirito.composeapp.generated.resources.descanso_acordado
import kirito.composeapp.generated.resources.lic_lz
import kirito.composeapp.generated.resources.lic_lza
import kirito.composeapp.generated.resources.lic_especial
import kirito.composeapp.generated.resources.ausencia_maternidad_paternidad
import kirito.composeapp.generated.resources.cambiado_con_
import kirito.composeapp.generated.resources.dia_sin_asignar
import kirito.composeapp.generated.resources.el_dia_anterior_tienes_x
import kirito.composeapp.generated.resources.el_dia_siguiente_tienes_x
import kirito.composeapp.generated.resources.haces_el_turno_a_
import kirito.composeapp.generated.resources.prop_cambio_dia_anterior
import kirito.composeapp.generated.resources.prop_cambio_dia_siguiente
import kirito.composeapp.generated.resources.suspension_empleo_sueldo
import kirito.composeapp.generated.resources.te_hace_el_turno_
import kirito.composeapp.generated.resources.tienes_xtiempo_entre_turnos
import kirito.composeapp.generated.resources.turno_cambiado
import kirito.composeapp.generated.resources.vacaciones
import kirito.composeapp.generated.resources.vacaciones_ano_pasado
import kirito.composeapp.generated.resources.turno_sin_trabajo_asignado
import kirito.composeapp.generated.resources.turno_al_debe
import kirito.composeapp.generated.resources.turno_oculto
import kirito.composeapp.generated.resources.turnos_cambiables
import kirito.composeapp.generated.resources.un_dia
import kirito.composeapp.generated.resources.zcon
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource

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


/** Devuelve true si el turno tiene LIBRas o COMJs **/
fun CuDetalleConFestivoDBModel.esTurnoConDias(): Boolean {
    if (this.libra != null && this.libra!! > 0)
        return true
    if (this.comj != null && this.comj!! > 0)
        return true
    return false
}

/** Devuelve true si el turno tiene LIBRas o COMJs **/
fun TurnoPrxTr.esTurnoConDias(): Boolean {
    if (this.libra != null && this.libra!! > 0)
        return true
    if (this.comj != null && this.comj!! > 0)
        return true
    return false
}

/** Devuelve true si el turno tiene LIBRas o COMJs **/
fun CuDetalle.esTurnoConDias(): Boolean {
    if (this.libra != null && this.libra!! > 0)
        return true
    if (this.comj != null && this.comj!! > 0)
        return true
    return false
}

fun GrTarea.servicio(): String{
    if (this.observaciones != null && this.observaciones?.length != 0) {
        if (this.servicio == "PS") {
            return this.observaciones.toString()
        }
    }
    return this.servicio
}


/** Devuelve la hora inicial de un sietemil. **/
fun String.hOrigenSietemil(): LocalTime {
    var horas = 0
    var minutos = 0

    //Compruebo que no me haya llegado un texto en vez de números.
    if (this.length > 3 && this.toIntOrNull() != null) {
        horas = this.substring(1, 3).toInt()
        minutos = if (this.dropLast(3) == "8")
            30
        else
            0
    }
    while (horas > 23)
        horas -= 24
    return LocalTime(horas, minutos)
}

/** Devuelve la hora final de un sietemil. **/
fun TurnoPrxTr.hFinSietemil(): LocalTime {
    var horas = 0
    var minutos = 0

    //Compruebo que no me haya llegado un texto en vez de números.
    if (this.turno.length > 3 && this.turno.toIntOrNull() != null) {
        horas = this.turno.substring(1, 3).toInt() + this.turno.drop(3).toInt()
        minutos = if (this.turno.dropLast(3) == "8")
            30
        else
            0
    }
    while (horas > 23)
        horas -= 24
    return LocalTime(horas, minutos)
}


/** Devuelve la hora final de un sietemil. **/
fun String.hFinSietemil(): LocalTime {
    var horas = 0
    var minutos = 0

    //Compruebo que no me haya llegado un texto en vez de números.
    if (this.length > 3 && this.toIntOrNull() != null) {
        horas = this.substring(1, 3).toInt() + this.drop(3).toInt()
        minutos = if (this.dropLast(3) == "8")
            30
        else
            0
    }
    while (horas > 23)
        horas -= 24
    return LocalTime(horas, minutos)
}


fun String.esTipoTurnoCambiable(): Boolean {
    return (turnosCambiables.contains(this))
}

/** Genera el texto para cuando un turno es cambiado. **/
@Composable
fun genNombreTextView(turno: TurnoPrxTr): String {
    return if (turno.tipo.esTipoTurnoCambiable()) {
        when (turno.tipo) {
            "DAD" -> stringResource(Res.string.te_hace_el_turno_) + turno.nombreDebe
            "TAD" -> stringResource(Res.string.haces_el_turno_a_) + turno.nombreDebe
            else -> if (turno.nombreDebe.isNullOrBlank()) ""
            else stringResource(Res.string.cambiado_con_) + turno.nombreDebe
        }
    } else
        ""
}

/** Genera el texto para cuando un turno es cambiado. **/
@Composable
fun genNombreTextView(turno: CuDetalleConFestivoDBModel): String {
    return if (turno.tipo.esTipoTurnoCambiable()) {
        when (turno.tipo) {
            "DAD" -> stringResource(Res.string.te_hace_el_turno_) + turno.nombreDebe
            "TAD" -> stringResource(Res.string.haces_el_turno_a_) + turno.nombreDebe
            else -> if (turno.nombreDebe.isNullOrBlank()) ""
            else stringResource(Res.string.cambiado_con_) + turno.nombreDebe
        }
    } else
        ""
}



@Composable
fun GrTarea.textoServicio(): String {
    val descTareaK = stringArrayResource(Res.array.descripcion_tareas_keys)
    val descTareaV = stringArrayResource(Res.array.descripcion_tareas_values)
    val miMap = descTareaK.zip(descTareaV).toMap()
    var texto: String

    val descripTarea = miMap[this.servicio]
    texto = if (descripTarea.isNotNullNorBlank()) {
        descripTarea ?: "" //Nunca es null aquí.
    } else {
        val prefijo = this.tipoServicio.prefijoTipoServicio()
        prefijo + this.servicio
    }
    if (this.observaciones != null && this.observaciones?.length != 0) {
        if (this.servicio == "PS") {
            texto = this.observaciones.toString()
        } else {
            texto += (" " + this.observaciones.toString())
        }
    }
    return texto
}

@Composable
fun String?.prefijoTipoServicio(): String {
    return when (this) {
        "T" -> stringResource(Res.string.tren_)
        "M" -> stringResource(Res.string.paso_maniobra_)
        else -> this.toString()
    }
}

@Composable
fun nombreTurnosConTipo(tipo: String, turno: String): String {
    var salida = tipo.nombreLargoTiposDeTurnos()
    if (turno.toIntOrNull() != null || tipo != turno)
        salida += " $turno"
    if (tipo == "T" && turno == "-")//Cuando un turno viene oculto, se ve así.
        return stringResource(Res.string.turno_oculto)
    return salida
}


/** Funciona solo con el turno, sin el tipo. **/
@Composable
fun String.nombreTurnosSinTipo(): String {
    return if (toIntOrNull() == null)
        nombreLargoTiposDeTurnos()
    else
        stringResource(Res.string.turno) + " " + this
}


@Composable
fun String.nombreLargoTiposDeTurnos(): String {
    when (this) {
        "7000" -> return stringResource(Res.string.turno)
        "CF" -> return stringResource(Res.string.curso_de_formacion)
        "CGO" -> return stringResource(Res.string.cedido_cgo)
        "COMJ" -> return stringResource(Res.string.compensacion_de_jornada)
        "D" -> return stringResource(Res.string.descanso)
        "DAD" -> return stringResource(Res.string.descanso_al_debe)
        "DD" -> return stringResource(Res.string.descanso_detraible)
        "DCOM" -> return stringResource(Res.string.descanso_compensado)
        "DH" -> return stringResource(Res.string.huelga_notificada)
        "DJ" -> return stringResource(Res.string.compensacion_dj)
        "DJA" -> return stringResource(Res.string.compensacion_dja)
        "E" -> return stringResource(Res.string.baja_enfermedad_comun)
        "ESP" -> return stringResource(Res.string.turno_especial)
        "AT" -> return stringResource(Res.string.accidente_trabajo_tren)
        "ITSB" -> return stringResource(Res.string.reposo_domiciliario)
        "FA" -> return stringResource(Res.string.formacion_adaptativa)
        "RHVE" -> return stringResource(Res.string.reciclaje_habilitacion_material)
        "FS" -> return stringResource(Res.string.funciones_sindicales)
        "H" -> return stringResource(Res.string.baja_accidente_laboral)
        "JT" -> return stringResource(Res.string.jornada_de_transicion)
        "LE" -> return stringResource(Res.string.lic_enfermedad_familiar)
        "LF" -> return stringResource(Res.string.lic_fallecimiento_familiar)
        "LH" -> return stringResource(Res.string.lic_nacimiento)
        "LK" -> return stringResource(Res.string.lic_traslado_domicilio)
        "LM" -> return stringResource(Res.string.lic_matrimonio)
        "LP" -> return stringResource(Res.string.lic_deber_inexcusable)
        "LR" -> return stringResource(Res.string.lic_examen_renfe)
        "LT" -> return stringResource(Res.string.lic_cambio_domicilio)
        "LX" -> return stringResource(Res.string.lic_examen_oficial)
        "LY" -> return stringResource(Res.string.lic_movilidad_geografica)
        "RMTC" -> return stringResource(Res.string.rec_mantenimiento_titulo)
        "RSIM" -> return stringResource(Res.string.rec_mant_titulo_simulador)
        "RM" -> return stringResource(Res.string.rec_medico)
        "LIBRa" -> return stringResource(Res.string.descanso_acordado)
        "LN" -> return stringResource(Res.string.lic_lactancia)
        "LB" -> return stringResource(Res.string.lic_fallecimiento_familiar_no_directo)
        "LS" -> return stringResource(Res.string.lic_sin_sueldo)
        "LV" -> return stringResource(Res.string.lic_sin_sueldo_asuntos_propios)
        "LZ" -> return stringResource(Res.string.lic_lz)
        "LZA" -> return stringResource(Res.string.lic_lza)
        "LZE" -> return stringResource(Res.string.lic_especial)
        "MD" -> return stringResource(Res.string.ausencia_maternidad_paternidad)
        "N/A", "NA" -> return stringResource(Res.string.dia_sin_asignar)
        "S" -> return stringResource(Res.string.suspension_empleo_sueldo)
        "T" -> return stringResource(Res.string.turno)
        "TAD" -> return stringResource(Res.string.turno_al_debe)
        "TC" -> return stringResource(Res.string.turno_cambiado)
        "VB" -> return stringResource(Res.string.vacaciones)
        "VL" -> return stringResource(Res.string.vacaciones_ano_pasado)
        "Z0OC" -> return stringResource(Res.string.turno_sin_trabajo_asignado)
        "ZCON" -> return stringResource(Res.string.zcon)
        "ZRA5" -> return stringResource(Res.string.descanso_compensacion)
        "ZRA6" -> return stringResource(Res.string.festivo_semanales)
        "" -> return ""
        "-" -> return stringResource(Res.string.turno_oculto)
        else -> return stringResource(Res.string.no_encontrado)
    }
}

fun colorTextoTurnos(tipo: String): Color {
    return when (tipo) {
        "COMJ", "DAD", "DCOM", "DD", "ESP" -> Color.White
        "" -> Color.Transparent
        else -> Color.Black
    }
}


fun colorDeFondoTurnos(tipo: String): String {
    when (tipo) {
        "7000" -> return "#C0C0C0"
        "CF" -> return "#97CC00"
        "COMJ" -> return "#0404FF"
        "D" -> return "#009900"
        "DAD" -> return "#007300"
        "DD" -> return "#A50021"
        "DCOM" -> return "#960096"
        "DJ", "DJA" -> return "#99CDFF"
        "E", "AT", "ITSB" -> return "#FFFF00"
        "ESP" -> return "#965A00"
        "FA", "RHVE" -> return "#009B00"
        "FS", "H", "RSIM", "RM", "CGO", "DH", "LE", "LF", "LH", "LK", "LM", "LP", "LR",
        "LT", "LX", "LY", "RMTC", "ZRA6" -> return "#F5F5DB"
        "LIBRa" -> return "#64FF32"
        "LN", "LB", "LS", "LV" -> return "#FE9A00"
        "LZ", "LZA", "JT" -> return "#99CDFF"
        "LZE" -> return "#66FFFF"
        "MD" -> return "#9B32FF"
        "N/A", "NA" -> return "#FFCCE5"
        "S" -> return "#999964"
        "T" -> return "#FFFFFF"
        "TAD" -> return "#00C8C8"
        "TC" -> return "#CCFFFF"
        "VB", "VL" -> return "#3399FF"
        "Z0OC" -> return "#66FF66"
        "ZCON" -> return "#999999"
        "ZRA5" -> return "#9B9BFF"
        "" -> return "#00000000"//Transparente.
        else -> return "#FFFFFF"
    }
}


fun CuDetalleConFestivoDBModel.toTurnoPrxTr(): TurnoPrxTr {
    return TurnoPrxTr(
        idDetalle = this.idDetalle,
        idUsuario = 0,
        fecha = this.fecha ?: 0,
        turno = this.turno,
        tipo = this.tipo,
        notas = this.notas,
        nombreDebe = this.nombreDebe,
        idGrafico = 0,
        sitioOrigen = "",
        horaOrigen = 0,
        sitioFin = "",
        horaFin = 0,
        diaSemana = "",
        libra = this.libra,
        comj = this.comj,
        indicador = 0,
        equivalencia = "",
        color = 0,
    )
}

@Composable
fun fraseDescansoAntes(tAyer: TurnoPrxTr, tHoy: TurnoPrxTr): String{
    var warningColor = "N"
    val diferenciaHoras = tAyer.deAyerAHoy(tHoy.horaOrigen)

    if (tAyer.esTurnoDeTrabajo()){
        var texto = stringResource(
            Res.string.prop_cambio_dia_anterior,
            nombreTurnosConTipo(
                tAyer.tipo,
                tAyer.turno).lowercase(),
            tAyer.horaFin?.toLocalTime().toString()
        )

        if (diferenciaHoras > -1) {
            val diferenciaHorasText = if (diferenciaHoras < 86400) {
                if (diferenciaHoras < 43200)
                    warningColor = "R"
                else if (diferenciaHoras < 50400)
                    warningColor = "A"
                stringResource(
                    Res.string.tienes_xtiempo_entre_turnos,
                    diferenciaHoras.toLocalTime()
                )
            } else {
                stringResource(
                    Res.string.tienes_xtiempo_entre_turnos,
                    stringResource(Res.string.un_dia) + (diferenciaHoras - 86400).toLocalTime()
                )
            }
            if (warningColor == "R" || warningColor == "A") {
                val spannableTexto = "$texto$diferenciaHorasText"
                val start = texto.length
                val end = spannableTexto.length
                //TODO: Meter los colores que no tiene.
                return spannableTexto
            } else {
                //Este texto perdería los colores si los tuviese por el concat().
                // Nos da igual porque no hay que colorear aquí.
                return texto + diferenciaHorasText
            }
        }
        return texto

    }else{
        return stringResource(
            Res.string.el_dia_anterior_tienes_x,
            tAyer.tipo.nombreLargoTiposDeTurnos().lowercase()
        )
    }

}

@Composable
fun fraseDescansoDespues(tHoy: TurnoPrxTr, tManana: TurnoPrxTr): String{
    var warningColor = "N"
    val pasaPorMedianoche = tHoy.pasaPorMedianoche()
    val diferenciaHoras = tManana.deMananaAHoy(tHoy.horaFin, pasaPorMedianoche)

    if (tManana.esTurnoDeTrabajo()){
        val texto = stringResource(
            Res.string.prop_cambio_dia_siguiente,
            nombreTurnosConTipo(
                tManana.tipo,
                tManana.turno).lowercase(),
            tManana.horaOrigen?.toLocalTime().toString()
        )

        if (diferenciaHoras > -1) {
            val diferenciaHorasText = if (diferenciaHoras < 86400) {
                if (diferenciaHoras < 43200)
                    warningColor = "R"
                else if (diferenciaHoras < 50400)
                    warningColor = "A"
                stringResource(
                    Res.string.tienes_xtiempo_entre_turnos,
                    diferenciaHoras.toLocalTime()
                )
            } else {
                stringResource(
                    Res.string.tienes_xtiempo_entre_turnos,
                    stringResource(Res.string.un_dia) + (diferenciaHoras - 86400).toLocalTime()
                )
            }
            if (warningColor == "R" || warningColor == "A") {
                val spannableTexto = "$texto$diferenciaHorasText"
                val start = texto.length
                val end = spannableTexto.length
                //TODO: Meter los colores que no tiene.
                return spannableTexto
            } else {
                //Este texto perdería los colores si los tuviese por el concat().
                // Nos da igual porque no hay que colorear aquí.
                return texto + diferenciaHorasText
            }
        }
        return texto

    }else{
        return stringResource(
            Res.string.el_dia_siguiente_tienes_x,
            tManana.tipo.nombreLargoTiposDeTurnos().lowercase()
        )
    }

}
