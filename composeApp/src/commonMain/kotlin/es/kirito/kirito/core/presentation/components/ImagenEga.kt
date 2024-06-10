package es.kirito.kirito.core.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import es.kirito.kirito.core.data.database.GrTareas
import es.kirito.kirito.core.data.database.OtColoresTrenes
import es.kirito.kirito.core.domain.util.media
import es.kirito.kirito.core.domain.util.minusHours
import es.kirito.kirito.core.domain.util.restar24
import es.kirito.kirito.core.domain.util.restar60
import es.kirito.kirito.core.domain.util.toComposeColor
import es.kirito.kirito.core.domain.util.toLocalDate
import es.kirito.kirito.core.domain.util.toMinuteOfDay
import es.kirito.kirito.core.domain.util.withLeadingZeros
import es.kirito.kirito.core.presentation.theme.colorTextoContraste
import es.kirito.kirito.core.presentation.theme.customColors
import es.kirito.kirito.core.presentation.utils.pxToDP
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalTime
import kotlinx.datetime.minus


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
    // val height: Int = 75 * factorZoom
    val height: Int = 82 * factorZoom
    val alturaLineas = 50f * factorZoom
    val alturaSitios = 33f * factorZoom
    val alturaServicios = 23f * factorZoom
    val alturaServicios2 = 13f * factorZoom
    val alturaServicios3 = 3f * factorZoom
    val alturaHoras = 53f * factorZoom
    val alturaTextHoras = 65f * factorZoom
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
    ).pxToDP()

    println("El ancho será $width")

    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = Modifier
        .height(height.pxToDP())
        .width(width),
        onDraw = {
            drawTimeLines(
                timeInicio,
                timeFin,
                factorZoom,
                alturaHoras,
                textMeasurer,
                colorTextoContraste,
                alturaTextHoras
            )
            drawNowLine(
                dia = dia,
                timeInicio = timeInicio,
                timeFin = timeFin,
                alturaLineas = alturaLineas,
                alturaFinal = height.toFloat(),
                factorZoom = factorZoom,
            )
            drawServicios(
                tareas,
                timeInicio.toInt(),
                factorZoom,
                dMinEntreTextos,
                dMinEntreTextServicios,
                alturaSitios,
                alturaServicios,
                alturaServicios2,
                alturaServicios3,
                alturaHoras,
                segundosHastaMedianoche,
                textMeasurer,
                dDesplHoras,
                alturaLineas,
                coloresTrenes
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
    alturaTextHoras: Float,
) {
    val horaInicial = timeInicio / 3600
    val minutoInicial = (timeInicio % 3600) / 60 // calculate minutes

    var remainingMinutes = if (minutoInicial == 0L) {
        0 // already at 0 minutes
    } else {
        60 - minutoInicial
    }

    val finIteraciones = ((timeFin / 3600) + 1).restar24()
        .toInt() //Pintaremos 1 hora más, la segunda será en la que no se cumpla.
    var iteraciones = -1 //Empiezo en un número que no es una hora real.

    // while (remainingMinutes < minutesFin) {
    while (iteraciones != finIteraciones) {
        println("La iteración actual es $iteraciones y $finIteraciones")
        drawLine(
            color = colorTextoContraste.copy(alpha = 0.7f),
            start = Offset(remainingMinutes.toFloat() * factorZoom, 0f),
            end = Offset(remainingMinutes.toFloat() * factorZoom, alturaTextHoras),
            strokeWidth = Stroke.HairlineWidth
        )
        val textHour = (
                if (remainingMinutes % 60 == 0L)//Resto una hora, si no, aparecen las horas desfasadas cuando empiezas a en punto.
                    remainingMinutes / 60 + horaInicial - 1
                else
                    remainingMinutes / 60 + horaInicial
                ).restar24().toInt().withLeadingZeros()

        val textResult = textMeasurer.measure(textHour, style = TextStyle(fontSize = (4 * factorZoom).sp))
        val subOffset = textResult.size.width / 4 //Para centrar el número.
        drawText(
            textResult,
            topLeft = Offset((remainingMinutes - subOffset).toFloat() * factorZoom, alturaTextHoras)
        )

        remainingMinutes += 60L
        iteraciones = textHour.toInt()
    }
}


private fun DrawScope.drawNowLine(
    dia: Long?,
    timeInicio: Long,
    timeFin: Long,
    alturaLineas: Float,
    alturaFinal: Float,
    factorZoom: Int
) {
    val now = Clock.System.now()
    var dibujar = false
    val minutoActual = now.toMinuteOfDay()
    val minutoInicio = timeInicio / 60
    var pasaPorMedianoche = false
    pasaPorMedianoche = (timeInicio > timeFin)
    if (dia.toLocalDate() == now.toLocalDate() && !pasaPorMedianoche)
        dibujar = true//Pintamos el día si es hoy y no pasa por medianoche.
    else if (pasaPorMedianoche) {
        if (minutoInicio < minutoActual) {//Antes de medianoche.
            if (dia.toLocalDate() == now.toLocalDate())//Pintamos el día de hoy.
                dibujar = true
        } else {//Las 00 y después. Pintamos en el día de ayer.
            if (dia.toLocalDate() == now.toLocalDate().minus(1, DateTimeUnit.DAY))
                dibujar = true
        }
    }
    val hora = if (pasaPorMedianoche && now.toMinuteOfDay() < minutoInicio)
        (minutoActual + 1440 + 60 - minutoInicio)
    //El minuto, más los minutos de un día, más la hora de desfase, menos cuando empezó.
    else
        minutoActual + 60 - minutoInicio

    if (dibujar)
        drawLine(
            color = Color.Red.copy(alpha = 0.7f),
            start = Offset(
                hora.toFloat() * factorZoom,
                alturaLineas
            ),
            end = Offset(
                hora.toFloat() * factorZoom,
                alturaFinal
            ),
            strokeWidth = Stroke.HairlineWidth
        )
}


private fun DrawScope.drawServicios(
    tareas: List<GrTareas>,
    timeInicio: Int,
    factorZoom: Int,
    dMinEntreTextos: Int,
    dMinEntreTextServicios: Int,
    alturaSitios: Float,
    alturaServicios: Float,
    alturaServicios2: Float,
    alturaServicios3: Float,
    alturaHoras: Float,
    segundosHastaMedianoche: Long,
    textMeasurer: TextMeasurer,
    dDesplHoras: Int,
    alturaLineas: Float,
    coloresTrenes: List<OtColoresTrenes>?
) {
    if (tareas.isNotEmpty()) {
        var posicionServicioPrevio = 0f
        var posicionServicioPrevio2 = 0f
        var posicionFinTareaPrevia = 0f
        var posicionInicioTareaPrevia = 0f
        var sitioFinTareaPrevia = ""
        val minutosIniciales = (timeInicio % 3600) / 60
        var finActualNoPintado = false

        var posUltimaHoraPintada = 0f


        /** Está en minutos desde las 00 y se le resta 60 **/


        val inicioTurno = LocalTime.fromSecondOfDay(timeInicio).minusHours(1)

        tareas.forEachIndexed { index, tarea ->
            val inicioTarea = if (tarea.horaOrigen == null)
                LocalTime.fromSecondOfDay(0)
            else
                LocalTime.fromSecondOfDay(tarea.horaOrigen!!)
            val finTarea = if (tarea.horaFin == null)
                LocalTime.fromSecondOfDay(0)
            else
                LocalTime.fromSecondOfDay(tarea.horaFin!!)


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
                val nextInicioLT = LocalTime.fromSecondOfDay(nextTarea.horaOrigen!!)
                nextHInicio = if (nextInicioLT < inicioTurno) { //Pasa por la medianoche.
                    (nextInicioLT.toMinuteOfDay() + 60 + (segundosHastaMedianoche.toInt()) / 60) * factorZoom
                } else {
                    (nextInicioLT.toMinuteOfDay() - inicioTurno.toMinuteOfDay()) * factorZoom
                }
            }


            //Pintamos la línea.
            drawServicioLine(
                inicio = inicio.toFloat(),
                fin = fin.toFloat(),
                tarea = tarea,
                alturaLineas = alturaLineas,
                factorZoom = factorZoom,
                colorTextoContraste = colorTextoContraste,
                coloresTrenes = coloresTrenes
            )


            //Pintamos el lugar de origen:
            val mismoSitioYAnteriorMuyCerca =
                inicio - posicionFinTareaPrevia < dMinEntreTextos && sitioFinTareaPrevia == tarea.sitioOrigen


            val mismoSitioYSiguienteMuyCerca =
                if (nextHInicio == null || nextTarea?.sitioOrigen == null)
                    false
                else
                    nextHInicio - fin < dMinEntreTextos && nextTarea.sitioOrigen == tarea.sitioFin





            if (!tarea.servicio.contains("Deje")) { //Si la tarea no es un deje

                val textResult = textMeasurer.measure(tarea.sitioOrigen ?: "", style = TextStyle(fontSize = (4 * factorZoom).sp))
                val subOffset = textResult.size.width / 4 //Para centrar el número.

                if (mismoSitioYAnteriorMuyCerca)//Pintamos en medio de los dos sitios:
                    drawText(
                        textResult,
                        topLeft = Offset(
                            (media(
                                posicionFinTareaPrevia.toLong(),
                                inicio.toLong()
                            ) - subOffset) * factorZoom, alturaSitios
                        )
                    )
                else
                    drawText(
                        textResult,
                        topLeft = Offset((inicio.toFloat() - subOffset) * factorZoom, alturaSitios)
                    )
            }

            //Pintamos el lugar de destino, si no es una toma ni está cerca:
            if (!tarea.servicio.contains("Toma") && !mismoSitioYSiguienteMuyCerca) {
                val textResult = textMeasurer.measure(tarea.sitioFin ?: "", style = TextStyle(fontSize = (4 * factorZoom).sp))
                val subOffset = textResult.size.width / 4 //Para centrar el número.
                drawText(
                    textResult,
                    topLeft = Offset((fin.toFloat() - subOffset) * factorZoom, alturaSitios)
                )
            }


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

                val textResult = textMeasurer.measure(textoServicio, style = TextStyle(fontSize = (4 * factorZoom).sp))
                val subOffset = textResult.size.width / 4 //Para centrar el número.

                if (posicionServicio - posicionServicioPrevio > dMinEntreTextServicios) {
                    drawText(
                        textResult,
                        topLeft = Offset(
                            (posicionServicio - subOffset) * factorZoom,
                            alturaServicios
                        )
                    )
                    posicionServicioPrevio = posicionServicio
                } else if (posicionServicio - posicionServicioPrevio2 > dMinEntreTextServicios) {
                    drawText(
                        textResult,
                        topLeft = Offset(
                            (posicionServicio - subOffset) * factorZoom,
                            alturaServicios2
                        )
                    )
                    posicionServicioPrevio2 = posicionServicio
                } else {
                    drawText(
                        textResult,
                        topLeft = Offset(
                            (posicionServicio - subOffset) * factorZoom,
                            alturaServicios3
                        )
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

                val textoServicio =
                    (fin / factorZoom + (minutosIniciales - 60)).restar60().toString()
                val textResult = textMeasurer.measure(textoServicio, style = TextStyle(fontSize = (4 * factorZoom).sp))
                val subOffset = textResult.size.width / 4 //Para centrar el número.
                drawText(
                    textResult,
                    topLeft = Offset((posicionFinTextoTarea - subOffset) * factorZoom, alturaHoras)
                )
            } else {
                finActualNoPintado = true
            }
            //Pintamos la hora de Inicio de cada tarea:
            //Lo desplazamos a la derecha si se ha pintado muy cerca el fin anterior.
            if (posicionInicioTextoTarea - posUltimaHoraPintada < dMinEntreTextos) {
                posicionInicioTextoTarea += dDesplHoras
            }
            val textoServicio =
                (inicio / factorZoom + (minutosIniciales - 60)).restar60().toString()
            val textResult = textMeasurer.measure(textoServicio, style = TextStyle(fontSize = (4 * factorZoom).sp))
            val subOffset = textResult.size.width / 4 //Para centrar el número.
            drawText(
                textResult,
                topLeft = Offset((posicionInicioTextoTarea - subOffset) * factorZoom, alturaHoras)
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


private fun DrawScope.drawServicioLine(
    inicio: Float,
    fin: Float,
    tarea: GrTareas,
    alturaLineas: Float,
    factorZoom: Int,
    colorTextoContraste: Color,
    coloresTrenes: List<OtColoresTrenes>?
) {
    if (tarea.servicio.contains("Toma") || tarea.servicio.contains("Deje")) {
        drawLine(
            color = colorTextoContraste,
            start = Offset(inicio, alturaLineas - (2.5f * factorZoom)),
            end = Offset(fin, alturaLineas - (2.5f * factorZoom)),
            strokeWidth = Stroke.HairlineWidth
        )
        drawLine(
            color = colorTextoContraste,
            start = Offset(inicio, alturaLineas + (2.5f * factorZoom)),
            end = Offset(fin, alturaLineas + (2.5f * factorZoom)),
            strokeWidth = Stroke.HairlineWidth
        )

    } else if (tarea.servicio == "AC" || tarea.servicio.startsWith("AC ")) {
        var inicioPintar = inicio
        do {
            if (inicioPintar.toInt() % (4 * factorZoom) == 0)
                Path().apply {
                    moveTo(inicioPintar, alturaLineas - (2.5f * factorZoom))
                    lineTo(inicioPintar, alturaLineas + (2.5f * factorZoom))
                    lineTo(inicioPintar + 2 * factorZoom, alturaLineas + (2.5f * factorZoom))
                    lineTo(inicioPintar + 2 * factorZoom, alturaLineas - (2.5f * factorZoom))
                    close()
                }.let { path ->
                    drawPath(
                        path = path,
                        color = colorTextoContraste
                    )
                }
            inicioPintar++
        } while (inicioPintar < fin)

    } else if (tarea.servicio == "SC") {
        var inicioPintar = inicio
        do {
            if (inicioPintar.toInt() % (4 * factorZoom) == 0)
                Path().apply {
                    moveTo(inicioPintar + (4 * factorZoom), alturaLineas - (2.5f * factorZoom))
                    lineTo(inicioPintar + (4 * factorZoom), alturaLineas + (2.5f * factorZoom))
                    lineTo(inicioPintar, alturaLineas + (2.5f * factorZoom))
                    lineTo(inicioPintar, alturaLineas - (2.5f * factorZoom))
                    close()
                }.let { path ->
                    drawPath(
                        path = path,
                        color = colorTextoContraste
                    )
                }
            inicioPintar++
        } while (inicioPintar < fin)

    } else if (tarea.servicio == "PS") {
        var inicioPintar = inicio
        do {
            if (inicioPintar.toInt() % (10 * factorZoom) == 0)
                Path().apply {
                    moveTo(inicioPintar, alturaLineas - (2.5f * factorZoom))
                    lineTo(inicioPintar, alturaLineas + (2.5f * factorZoom))
                    lineTo(inicioPintar + 5f * factorZoom, alturaLineas + (2.5f * factorZoom))
                    lineTo(inicioPintar + 5f * factorZoom, alturaLineas - (2.5f * factorZoom))
                    close()
                }.let { path ->
                    drawPath(
                        path = path,
                        color = colorTextoContraste
                    )
                }
            inicioPintar++
        } while (inicioPintar < fin)

    } else if (tarea.servicio.startsWith("V")) {
        var inicioPintar = inicio
        do {
            if (inicioPintar.toInt() % (4 * factorZoom) == 0) {
                drawLine(
                    color = colorTextoContraste,
                    start = Offset(
                        inicioPintar + (4 * factorZoom),
                        alturaLineas - (2.5f * factorZoom)
                    ),
                    end = Offset(inicioPintar, alturaLineas + (2.5f * factorZoom)),
                )
                drawLine(
                    color = colorTextoContraste,
                    start = Offset(inicioPintar, alturaLineas - (2.5f * factorZoom)),
                    end = Offset(
                        inicioPintar + (4 * factorZoom),
                        alturaLineas + (2.5f * factorZoom)
                    ),
                )
            }
            inicioPintar++
        } while (inicioPintar < fin)

    } else {//Tareas normales:

        val color = if (!coloresTrenes.isNullOrEmpty())
            tarea.getServicioColor(coloresTrenes)
        else
            null
        Path().apply {
            moveTo(inicio, alturaLineas - (2.5f * factorZoom))
            lineTo(inicio, alturaLineas + (2.5f * factorZoom))
            lineTo(fin, alturaLineas + (2.5f * factorZoom))
            lineTo(fin, alturaLineas - (2.5f * factorZoom))
            close()
        }.let { path ->
            drawPath(
                path = path,
                color = color ?: Color.Green
            )
        }
    }
}

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

