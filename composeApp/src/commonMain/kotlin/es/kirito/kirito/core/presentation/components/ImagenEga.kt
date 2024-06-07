package es.kirito.kirito.core.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import es.kirito.kirito.core.data.database.GrTareas
import es.kirito.kirito.core.data.database.OtColoresTrenes
import es.kirito.kirito.core.domain.util.restar24
import es.kirito.kirito.core.domain.util.toComposeColor
import es.kirito.kirito.core.domain.util.withLeadingZeros
import es.kirito.kirito.core.presentation.theme.customColors


@Composable
fun ImagenEga(
    timeInicio: Long,
    timeFin: Long,
    tareas: List<GrTareas>,
    dia: Long?,
    coloresTrenes: List<OtColoresTrenes>?,
    factorZoom: Int = 2,
    textColor: Int? = null,
) {
    val height: Int = 75 * factorZoom
    val alturaLineas = 50f * factorZoom
    val alturaSitios = 43f * factorZoom
    val alturaServicios = 33f * factorZoom
    val alturaServicios2 = 23f * factorZoom
    val alturaServicios3 = 13f * factorZoom
    val alturaHoras = 65f * factorZoom
    val alturaTextHoras = 75f * factorZoom
    val esNoche = timeInicio < timeFin
    val segundosHastaMedianoche = if (esNoche) 86400 - timeInicio else 0L
    val dMinEntreTextos = 15 * factorZoom
    val dDesplHoras = 6 * factorZoom
    val dMinEntreTextServicios = 50 * factorZoom
    val myTextSize = 12f * factorZoom
    val colorTextoContraste = MaterialTheme.customColors.textoContraste


    val width = getWidth(
        timeInicio,
        timeFin,
        esNoche,
        segundosHastaMedianoche,
        factorZoom
    ).dp

    println("El ancho será $width")

    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = Modifier
        .height(height.dp)
        .width(width),
        onDraw = {
            drawTimeLines(
                timeInicio,
                timeFin,
                factorZoom,
                alturaHoras,
                textMeasurer,
                colorTextoContraste
            )
        }
    )
}

private fun DrawScope.drawTimeLines(
    timeInicio: Long,
    timeFin: Long,
    factorZoom: Int,
    alturaHoras: Float,
    textMeasurer: TextMeasurer,
    colorTextoContraste: Color,
) {
    val horaInicial = timeInicio / 3600
    val minutoInicial = (timeInicio % 3600) / 60 // calculate minutes

    var remainingMinutes = if (minutoInicial == 0L) {
        0 // already at 0 minutes
    } else {
        60 - minutoInicial
    }
    val minutesFin = timeFin / 60
    println("RemainingMinutes start: $remainingMinutes, $timeFin")

    while (remainingMinutes < minutesFin) {
        drawLine(
            color = colorTextoContraste.copy(alpha = 0.7f),
            start = Offset(remainingMinutes.toFloat() * factorZoom, 0f),
            end = Offset(remainingMinutes.toFloat() * factorZoom, alturaHoras),
            strokeWidth = Stroke.HairlineWidth
        )
        val textHour = (
                if (remainingMinutes % 60 == 0L)//Resto una hora, si no, aparecen las horas desfasadas cuando empiezas a en punto.
                    remainingMinutes / 60 + horaInicial - 1
                else
                    remainingMinutes / 60 + horaInicial
                ).restar24().toInt().withLeadingZeros()

        val textResult = textMeasurer.measure(textHour)
        val subOffset = textResult.size.width / 4 //Para centrar el número.
        drawText(textResult, topLeft = Offset((remainingMinutes - subOffset).toFloat() * factorZoom, alturaHoras))

        println("remaininMinutes before $remainingMinutes")
        remainingMinutes += 60L
        println("remaininMinutes after $remainingMinutes")


    }

}

/*



private fun drawNowLine(dia: Long?) {
    var dibujar = false
    val minutoActual = LocalTime.now().toMinuteOfDay()
    val minutoInicio = if (timeInicio != null) timeInicio / 60 else 0
    var pasaPorMedianoche = false
    if (timeInicio != null && timeFin != null) {
        pasaPorMedianoche = (timeInicio > timeFin)
        if (dia.toLocalDate() == LocalDate.now() && !pasaPorMedianoche)
            dibujar = true//Pintamos el día si es hoy y no pasa por medianoche.
        else if (pasaPorMedianoche) {
            if (minutoInicio < minutoActual) {//Antes de medianoche.
                if (dia.toLocalDate() == LocalDate.now())//Pintamos el día de hoy.
                    dibujar = true
            } else {//Las 00 y después. Pintamos en el día de ayer.
                if (dia.toLocalDate() == LocalDate.now().minusDays(1))
                    dibujar = true
            }
        }
    }
    val hora = if (timeInicio != null) {
        //Si es después de la medianoche en un turno nocturno:
        if (pasaPorMedianoche && LocalTime.now().toMinuteOfDay() < minutoInicio)
            (minutoActual + 1440 + 60 - minutoInicio)
        //El minuto, más los minutos de un día, más la hora de desfase, menos cuando empezó.
        else
            minutoActual + 60 - minutoInicio

    } else
        0

    if (dibujar)
        canvas.drawLine(
            hora.toFloat() * factorZoom,
            alturaLineas,
            hora.toFloat() * factorZoom,
            alturaTextHoras,
            createThinColorPaint(Color.RED, 50)
        )

}


private fun drawServicios(tareas: List<GrTareas>?) {
    if (!tareas.isNullOrEmpty() && timeInicio != null) {
        var posicionServicioPrevio = 0f
        var posicionServicioPrevio2 = 0f
        var posicionFinTareaPrevia = 0f
        var posicionInicioTareaPrevia = 0f
        var sitioFinTareaPrevia = ""
        val minutosIniciales = (timeInicio % 3600) / 60
        var finActualNoPintado = false

        var posUltimaHoraPintada = 0f

        */
/** Está en minutos desde las 00 y se le resta 60 **//*

        val inicioTurno = LocalTime.ofSecondOfDay(timeInicio).minusHours(1)

        tareas.forEachIndexed { index, tarea ->
            val inicioTarea = if (tarea.horaOrigen == null)
                LocalTime.ofSecondOfDay(0)
            else
                LocalTime.ofSecondOfDay(tarea.horaOrigen!!.toLong())
            val finTarea = if (tarea.horaFin == null)
                LocalTime.ofSecondOfDay(0)
            else
                LocalTime.ofSecondOfDay(tarea.horaFin!!.toLong())


            val inicio = if (inicioTarea < inicioTurno) { //Pasa por la medianoche.
                (inicioTarea.toMinuteOfDay() + 60 + (segundosHastaMedianoche.toInt()) / 60) * factorZoom
            } else {
                (inicioTarea.toMinuteOfDay() - inicioTurno.toMinuteOfDay()) * factorZoom
            }
            val fin = if (finTarea < inicioTurno) {
                (finTarea.toMinuteOfDay() + 60 + (segundosHastaMedianoche).toInt() / 60) * factorZoom
            } else {
                (finTarea.toMinuteOfDay() - inicioTurno.toMinuteOfDay()) * factorZoom
            }

            var nextHInicio: Int? = null
            val nextTarea = if (index < tareas.size - 1) {
                tareas[index + 1] // get the next item if there is one
            } else {
                null // there is no next item
            }
            if (nextTarea?.horaOrigen != null) {
                val nextInicioLT = LocalTime.ofSecondOfDay(nextTarea.horaOrigen!!.toLong())
                nextHInicio = if (nextInicioLT < inicioTurno) { //Pasa por la medianoche.
                    (nextInicioLT.toMinuteOfDay() + 60 + (segundosHastaMedianoche.toInt()) / 60) * factorZoom
                } else {
                    (nextInicioLT.toMinuteOfDay() - inicioTurno.toMinuteOfDay()) * factorZoom
                }
            }


            //Pintamos la línea.
            drawServicioLine(inicio.toFloat(), fin.toFloat(), tarea, context)


            //Pintamos el lugar de origen:
            val mismoSitioYAnteriorMuyCerca =
                inicio - posicionFinTareaPrevia < dMinEntreTextos && sitioFinTareaPrevia == tarea.sitioOrigen


            val mismoSitioYSiguienteMuyCerca =
                if (nextHInicio == null || nextTarea?.sitioOrigen == null)
                    false
                else
                    nextHInicio - fin < dMinEntreTextos && nextTarea.sitioOrigen == tarea.sitioFin


            if (!tarea.servicio.contains("Deje")) //Si la tarea no es un deje
                if (mismoSitioYAnteriorMuyCerca)//Pintamos en medio de los dos sitios:
                    canvas.drawText(
                        tarea.sitioOrigen ?: "",
                        media(posicionFinTareaPrevia.toLong(), inicio.toLong()),
                        alturaSitios,
                        textPaint
                    )
                else
                    canvas.drawText(
                        tarea.sitioOrigen ?: "",
                        inicio.toFloat(),
                        alturaSitios,
                        textPaint
                    )

            //Pintamos el lugar de destino, si no es una toma ni está cerca:
            if (!tarea.servicio.contains("Toma") && !mismoSitioYSiguienteMuyCerca)
                canvas.drawText(tarea.sitioFin ?: "", fin.toFloat(), alturaSitios, textPaint)


            //Pintamos el servicio:
            //Si NO es una toma o un deje:
            if (!tarea.servicio.contains("Deje") && !tarea.servicio.contains("Toma")) {

                val posicionServicio = media(inicio.toLong(), fin.toLong())
                var textoServicio = tarea.servicio
                if (tarea.tipoServicio == "M")
                    textoServicio = "P" + tarea.servicio
                if (tarea.observaciones?.length != 0 && tarea.servicio == "PS")
                    textoServicio =
                        tarea.observaciones.toString().take(7)//Solo los siete primeros caracteres.

                if (posicionServicio - posicionServicioPrevio > dMinEntreTextServicios) {
                    canvas.drawText(textoServicio, posicionServicio, alturaServicios, textPaint)
                    posicionServicioPrevio = posicionServicio
                } else if (posicionServicio - posicionServicioPrevio2 > dMinEntreTextServicios) {
                    canvas.drawText(
                        textoServicio,
                        posicionServicio,
                        alturaServicios2,
                        textPaint
                    )
                    posicionServicioPrevio2 = posicionServicio
                } else {
                    canvas.drawText(
                        textoServicio,
                        posicionServicio,
                        alturaServicios3,
                        textPaint
                    )
                }
            }
            var posicionInicioTextoTarea = inicio.toFloat()


            //Pintamos la hora de fin de cada tarea:
            //Solo la pintamos si: No hay siguiente tarea. La distancia del inicio actual y de la siguiente
            // tarea es lo suficientemente grande. La hora no es idéntica a la de inicio siguiente.
            var posicionFinTextoTarea = fin.toFloat()
            if (fin - inicio > dMinEntreTextos && mismoSitioYSiguienteMuyCerca)
                posicionFinTextoTarea -= dDesplHoras
            if (nextHInicio == null || nextHInicio - posicionInicioTextoTarea > dMinEntreTextos * 2 &&
                nextHInicio != fin
            ) {
                finActualNoPintado = false

                canvas.drawText(
                    (fin / factorZoom + (minutosIniciales - 60)).restar60().toString(),
                    posicionFinTextoTarea,
                    alturaHoras,
                    textPaint
                )
            } else {
                finActualNoPintado = true
            }
            //Pintamos la hora de Inicio de cada tarea:
            //Lo desplazamos a la derecha si se ha pintado muy cerca el fin anterior.
            if (posicionInicioTextoTarea - posUltimaHoraPintada < dMinEntreTextos) {
                posicionInicioTextoTarea += dDesplHoras
            }
            canvas.drawText(
                (inicio / factorZoom + (minutosIniciales - 60)).restar60().toString(),
                posicionInicioTextoTarea,
                alturaHoras,
                textPaint
            )
            if (!finActualNoPintado)
                posUltimaHoraPintada =
                    posicionFinTextoTarea//Guardo la posición donde he pintado la hora de fin última.


            //Al final, guardamos valores para el siguiente bucle:
            sitioFinTareaPrevia = tarea.sitioFin ?: ""
            posicionFinTareaPrevia = fin.toFloat()
            posicionInicioTareaPrevia = inicio.toFloat()

        }
    }
}

private fun drawServicioLine(
    inicio: Float,
    fin: Float,
    tarea: GrTareas,
) {
    if (tarea.servicio.contains("Toma") || tarea.servicio.contains("Deje")) {
        canvas.drawLine(
            inicio,
            alturaLineas - (2.5f * factorZoom),
            fin,
            alturaLineas - (2.5f * factorZoom),
            createThinColorPaint(color = colorTextoContraste.toArgb())
        )

        canvas.drawLine(
            inicio,
            alturaLineas + (2.5f * factorZoom),
            fin,
            alturaLineas + (2.5f * factorZoom),
            createThinColorPaint(color = colorTextoContraste.toArgb())
        )


    } else if (tarea.servicio == "AC" || tarea.servicio.startsWith("AC ")) {
        var inicioPintar = inicio
        do {
            if (inicioPintar.toInt() % (4 * factorZoom) == 0)
                canvas.drawLine(
                    inicioPintar,
                    alturaLineas - (2.5f * factorZoom),
                    inicioPintar,
                    alturaLineas + (2.5f * factorZoom),
                    createMidColorPaint(color = colorTextoContraste.toArgb())
                )
            inicioPintar++
        } while (inicioPintar < fin)

    } else if (tarea.servicio == "SC") {
        var inicioPintar = inicio
        do {
            if (inicioPintar.toInt() % (4 * factorZoom) == 0)
                canvas.drawLine(
                    inicioPintar + (4 * factorZoom),
                    alturaLineas - (2.5f * factorZoom),
                    inicioPintar,
                    alturaLineas + (2.5f * factorZoom),
                    createThinColorPaint(color = colorTextoContraste.toArgb())
                )
            inicioPintar++
        } while (inicioPintar < fin)

    } else if (tarea.servicio == "PS") {
        var inicioPintar = inicio
        do {
            if (inicioPintar.toInt() % (10 * factorZoom) == 0)
                canvas.drawLine(
                    inicioPintar,
                    alturaLineas - (2.5f * factorZoom),
                    inicioPintar,
                    alturaLineas + (2.5f * factorZoom),
                    createColorPaint(color = colorTextoContraste.toArgb())
                )
            inicioPintar++
        } while (inicioPintar < fin)

    } else if (tarea.servicio.startsWith("V")) {
        var inicioPintar = inicio
        do {
            if (inicioPintar.toInt() % (4 * factorZoom) == 0) {
                canvas.drawLine(
                    inicioPintar + (4 * factorZoom),
                    alturaLineas - (2.5f * factorZoom),
                    inicioPintar,
                    alturaLineas + (2.5f * factorZoom),
                    createThinColorPaint(color = colorTextoContraste.toArgb())
                )
                canvas.drawLine(
                    inicioPintar,
                    alturaLineas - (2.5f * factorZoom),
                    inicioPintar + (4 * factorZoom),
                    alturaLineas + (2.5f * factorZoom),
                    createThinColorPaint(color = colorTextoContraste.toArgb())
                )
            }
            inicioPintar++
        } while (inicioPintar < fin)

    } else {//Tareas normales:

        val color = if (!coloresTrenes.isNullOrEmpty())
            tarea.getServicioColor(coloresTrenes)
        else
            null
        canvas.drawLine(
            inicio, alturaLineas, fin, alturaLineas, createColorPaint(color)
        )
    }

}
*/


private fun getWidth(
    segundosInicio: Long?,
    segundosFin: Long?,
    flagEsNoche: Boolean,
    segundosHastaMedianoche: Long,
    factorZoom: Int
): Int {
    if (segundosInicio != null && segundosFin != null) {
        val ancho = if (flagEsNoche) {
            (segundosFin - segundosInicio) / 60//Lo paso a minutos.
        } else {

            (segundosHastaMedianoche + segundosFin) / 60 //Si es mayor la hora inicio, pasa por medianoche.
        }
        if (ancho != 0L)
            return ((ancho + 120) * factorZoom).toInt()//Le sumo dos horas.
    }
    return 1//No puede ser de 0 de ancho.
}


private fun createThinColorPaint(color: Color? = null, alpha: Int? = null, factorZoom: Int): Paint {
    return Paint().apply {
        this.alpha = (alpha ?: 255).toFloat() / 255
        this.color = color ?: Color.Black
        style = PaintingStyle.Stroke
        strokeWidth = 1f * factorZoom
    }
}

private fun createMidColorPaint(color: Color? = null, alpha: Int? = null, factorZoom: Int): Paint {
    return Paint().apply {
        this.alpha = (alpha ?: 255).toFloat() / 255
        this.color = color ?: Color.Black
        style = PaintingStyle.Stroke
        strokeWidth = 2f * factorZoom
    }
}


private fun createColorPaint(color: Color? = null, alpha: Int? = null, factorZoom: Int): Paint {
    return Paint().apply {
        this.alpha = (alpha ?: 255).toFloat() / 255
        this.color = color ?: Color.Green
        style = PaintingStyle.Stroke
        strokeWidth = 5f * factorZoom
    }
}


private fun GrTareas.getServicioColor(
    coloresTrenes: List<OtColoresTrenes>
): Color? {
    val lista = coloresTrenes.toMutableList()
    lista.filterNot {
        it.filtro == "dfAc" || it.filtro == "dfSc" || it.filtro == "dfVj" || it.filtro == "dfPs"
    }
    //Filtramos por coincidencia exacta.
    val filtroExacto = lista.find { it.filtro == this.servicio }
    if (filtroExacto != null)
        return filtroExacto.color.toComposeColor()

    //Filtramos por inicio similar.
    val listaInicios = lista
        .filter { it.filtro.endsWith("*") }
        .map { OtColoresTrenes(it.filtro.removeSuffix("*"), it.color) }
    val listaIniciosFiltrada = listaInicios.filter { this.servicio.startsWith(it.filtro) }
    if (listaIniciosFiltrada.isNotEmpty())
        return listaIniciosFiltrada.maxBy { it.filtro.length }.color.toComposeColor()

    //Filtramos por final similar.
    val listaFinales = lista
        .filter { it.filtro.startsWith("*") }
        .map { OtColoresTrenes(it.filtro.removePrefix("*"), it.color) }
    val listaFinalesFiltrada = listaFinales.filter { this.servicio.endsWith(it.filtro) }
    if (listaFinalesFiltrada.isNotEmpty())
        return listaFinalesFiltrada.maxBy { it.filtro.length }.color.toComposeColor()

    //Filtramos por Origen y final concretos:
    val stringOYFConcretos = this.sitioOrigen + "-" + this.sitioFin
    val origenYFinConcr = lista.find { it.filtro == stringOYFConcretos }
    if (origenYFinConcr != null)
        return origenYFinConcr.color.toComposeColor()

    //Filtramos por Origen y final genéricos:
    val oYF1 = this.sitioOrigen + "*" + this.sitioFin
    val oYF2 = this.sitioFin + "*" + this.sitioOrigen
    val origenYFinGen = lista.find { it.filtro == oYF1 || it.filtro == oYF2 }
    if (origenYFinGen != null)
        return origenYFinGen.color.toComposeColor()

    //Filtramos por único sitio de origen o final:
    val origenOFin = lista.find { it.filtro == this.sitioOrigen || it.filtro == this.sitioFin }
    if (origenOFin != null)
        return origenOFin.color.toComposeColor()

    //Filtramos por valores por defecto.
    if (this.tipoServicio == "T") {
        val color = lista.find { it.filtro == "dfTr" }
        if (color != null)
            return color.color.toComposeColor()
    }

    if (this.tipoServicio == "M") {
        val color = lista.find { it.filtro == "dfOt" }
        if (color != null)
            return color.color.toComposeColor()
    }
    return null
}

