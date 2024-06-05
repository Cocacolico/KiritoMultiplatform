package es.kirito.kirito.core.domain

import es.kirito.kirito.core.data.constants.MyConstants
import es.kirito.kirito.core.data.dataStore.updatePreferenciasKirito
import es.kirito.kirito.core.data.database.CaPeticiones
import es.kirito.kirito.core.data.database.CuDetalle
import es.kirito.kirito.core.data.database.CuHistorial
import es.kirito.kirito.core.data.database.GrEquivalencias
import es.kirito.kirito.core.data.database.GrExcelIF
import es.kirito.kirito.core.data.database.GrNotasTren
import es.kirito.kirito.core.data.database.GrNotasTurno
import es.kirito.kirito.core.data.database.GrTareas
import es.kirito.kirito.core.data.database.KiritoDatabase
import es.kirito.kirito.core.data.network.KiritoRequest
import es.kirito.kirito.core.data.network.models.RequestComplementosGraficoDTO
import es.kirito.kirito.core.data.network.models.RequestSimpleDTO
import es.kirito.kirito.core.data.network.models.RequestUpdatedDTO
import es.kirito.kirito.core.domain.kiritoError.lanzarExcepcion
import es.kirito.kirito.core.domain.util.enFormatoDeSalida
import es.kirito.kirito.core.domain.util.fromDateStringToLong
import es.kirito.kirito.core.domain.util.fromDateTimeStringToLong
import es.kirito.kirito.core.domain.util.fromTimeStringToInt
import es.kirito.kirito.core.domain.util.fromTimeWOSecsStringToInt
import es.kirito.kirito.core.domain.util.toInstant
import es.kirito.kirito.core.domain.util.toMyBoolean
import es.kirito.kirito.core.domain.util.toStringIfNull
import es.kirito.kirito.precarga.data.network.models.RequestGraficoDTO
import es.kirito.kirito.precarga.data.network.models.ResponseCaPeticionesDTO
import es.kirito.kirito.precarga.data.network.models.ResponseCuDetallesDTO
import es.kirito.kirito.precarga.data.network.models.ResponseCuHistorialDTO
import es.kirito.kirito.precarga.data.network.models.ResponseEquivalenciasDTO
import es.kirito.kirito.precarga.data.network.models.ResponseExcelIfDTO
import es.kirito.kirito.precarga.data.network.models.ResponseGrTareasDTO
import es.kirito.kirito.precarga.data.network.models.ResponseNotasTrenDTO
import es.kirito.kirito.precarga.data.network.models.ResponseNotasTurnoDTO

import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CoreRepository : KoinComponent {
    private val database: KiritoDatabase by inject()
    private val dao = database.kiritoDao()
    private val ktor = KiritoRequest()

    suspend fun getUpdatedDB() = dao.getTableUpdateWOYear(MyConstants.GENERAL_UPLOAD)


    suspend fun nukeAll() {
        //TODO: Hacer todo lo que es de Android exclusivo.
        //  WorkManager.getInstance(context).cancelAllWork()
        //  ShortcutManagerCompat.removeAllDynamicShortcuts(context)
        logout()
        updatePreferenciasKirito {
            it.copy(
                matricula = "",
                password = "",
                lastAutoDcom = 0,
                autoDateDiasEsp = 0L,
                weatherUpdated = 0L,
                userId = 0L,
                residenciaName = "",
                residenciaURL = "",
                preLogin = false,
                token = "",//Esto va debajo de logout porque se usa el token en la petición.
            )
        }
        //TODO: Implementar el clearAllTables.
        // database.clearAllTables()
    }

    suspend fun logout() {
        try {
            RequestSimpleDTO("usuarios.logout").let { salida ->
                val respuesta = ktor.requestSimpleEmptyResponse(salida)
                respuesta.error.lanzarExcepcion()
            }
        } catch (e: Exception) {
            println(e.message.toString())
        }
    }

    suspend fun descargarComplementosDelGrafico(graficos: List<String>) {
        RequestComplementosGraficoDTO(
            peticion = "graficos.complementos.obtener",
            id_grafico = graficos
        ).let { salida ->
            val respuesta = ktor.requestComplementosGrafico(salida)
            respuesta.error.lanzarExcepcion()
            respuesta.respuesta?.forEach {complementos ->
                val idGrafico = complementos.idGrafico.toLong()
                dao.deleteGrExcelIF(idGrafico)
                complementos.excelIF.forEach { dao.insertGrExcelIF(it.asDatabaseModel()) }
                dao.deleteAGrTareas(idGrafico)
                complementos.grTareas.forEach { dao.insertGrTareas(it.asDatabaseModel()) }
                dao.deleteGrNotasTrenDelGrafico(idGrafico)
                complementos.notasTren.forEach { dao.insertGrNotasTren(it.asDatabaseModel()) }
                dao.deleteGrNotasTurnoDelGrafico(idGrafico)
                complementos.notasTurnos.forEach { dao.insertGrNotasTurno(it.asDatabaseModel()) }
                dao.deleteGrEquivalenciasDelGrafico(idGrafico)
                complementos.equivalencias.forEach { dao.insertGrEquivalencias(it.asDatabaseModel()) }
            }

        }
    }

    suspend fun descargarComplementosDelGrafico(idGrafico: Long) {
        refreshGrExcelIF(idGrafico)
        refreshGrTareas(idGrafico)
        refreshNotasTren(idGrafico)
        refreshNotasTurno(idGrafico)
        refreshEquivalencias(idGrafico)
    }

    private suspend fun refreshGrExcelIF(idGrafico: Long) {
        RequestGraficoDTO("excelif.obtener", idGrafico.toString()).let { salida ->
            val respuesta = ktor.requestExcelIf(salida)
            if (respuesta.error.lanzarExcepcion()) {
                dao.deleteGrExcelIF(idGrafico)
                respuesta.respuesta?.forEach {
                    dao.insertGrExcelIF(it.asDatabaseModel())
                }
            }
        }
    }

    private suspend fun refreshGrTareas(idGrafico: Long) {
        RequestGraficoDTO("excellibreta.obtener", idGrafico.toString()).let { salida ->
            val respuesta = ktor.requestGrTareas(salida)
            if (respuesta.error.lanzarExcepcion()) {
                dao.deleteAGrTareas(idGrafico)
                respuesta.respuesta?.forEach {
                    dao.insertGrTareas(it.asDatabaseModel())
                }
            }
        }
    }

    private suspend fun refreshNotasTren(idGrafico: Long) {
        RequestGraficoDTO("graficos.notas_trenes.obtener", idGrafico.toString()).let { salida ->
            val respuesta = ktor.requestNotasTren(salida)
            if (respuesta.error.lanzarExcepcion()) {
                dao.deleteGrNotasTrenDelGrafico(idGrafico)
                respuesta.respuesta?.forEach {
                    dao.insertGrNotasTren(it.asDatabaseModel())
                }
            }
        }
    }

    private suspend fun refreshNotasTurno(idGrafico: Long) {
        RequestGraficoDTO("graficos.notas_turnos.obtener", idGrafico.toString()).let { salida ->
            val respuesta = ktor.requestNotasTurno(salida)
            if (respuesta.error.lanzarExcepcion()) {
                dao.deleteGrNotasTurnoDelGrafico(idGrafico)
                respuesta.respuesta?.forEach {
                    dao.insertGrNotasTurno(it.asDatabaseModel())
                }
            }
        }
    }

    private suspend fun refreshEquivalencias(idGrafico: Long) {
        RequestGraficoDTO("graficos.equivalencias.obtener", idGrafico.toString()).let { salida ->
            val respuesta = ktor.requestEquivalencias(salida)
            if (respuesta.error.lanzarExcepcion()) {
                dao.deleteGrEquivalenciasDelGrafico(idGrafico)
                respuesta.respuesta?.forEach {
                    dao.insertGrEquivalencias(it.asDatabaseModel())
                }
            }
        }
    }

    suspend fun refreshCuDetalles(bdActualizada: Instant) {
        refreshComplementosTurnos(bdActualizada)
        val salida = RequestUpdatedDTO("turnos.obtener", bdActualizada.enFormatoDeSalida())
        val respuesta = ktor.requestCuDetalles(salida)
        if (respuesta.error.lanzarExcepcion())
            respuesta.respuesta?.forEach {
                dao.insertCuDetalles(it.asDatabaseModel())
            }
    }

    private suspend fun refreshComplementosTurnos(bdActualizada: Instant) {
        refreshHistorial(bdActualizada)
    }

    private suspend fun refreshHistorial(bdActualizada: Instant) {
        bdActualizada.enFormatoDeSalida().let { updatedString ->
            RequestUpdatedDTO("turnos.obtener_historial_anio", updatedString).let { salida ->
                val respuesta = ktor.requestHistorial(salida)
                if (respuesta.error.lanzarExcepcion()) {
                    respuesta.respuesta?.forEach {
                        dao.insertCuHistorial(it.asDatabaseModel())
                    }
                }
            }
        }
    }

    suspend fun refreshCambios(bdActualizada: Instant) {
        bdActualizada.enFormatoDeSalida().let { updatedString ->
            RequestUpdatedDTO("cambios.obtener", updatedString).let { salida ->
                val respuesta = ktor.requestCaPeticiones(salida)
                if (respuesta.error.lanzarExcepcion()) {
                    respuesta.respuesta?.forEach {
                        dao.upsertCaPeticiones(it.asDatabaseModel())
                    }
                }
            }
        }
    }


    suspend fun descargarCuadroAnual() {
        //TODO: Descargar asíncronamente algún día.
//        val workManager = WorkManager.getInstance(context)
//        val worker = OneTimeWorkRequest.Builder(DownloadCuadroWorker::class.java)
//        val constraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.CONNECTED)
//            .build()
//        worker
//            .addTag("BAJAR_CUADRO")
//            .setConstraints(constraints)
//            .build()
//        workManager.enqueue(worker.build())

        val bdActualizada = getUpdatedDB().toInstant()
        refreshCuDetalles(bdActualizada)
        //TODO:
        // refreshAlarmas()
        refreshCambios(bdActualizada)

    }
}

private fun ResponseCaPeticionesDTO.asDatabaseModel(): CaPeticiones {
    return CaPeticiones(
        id = id?.toLong() ?: 0,
        idUsuarioPide = idUsuarioPide?.toLong() ?: 0,
        idUsuarioRecibe = idUsuarioRecibe?.toLong() ?: 0,
        fecha = fecha?.fromDateStringToLong() ?: 0,
        dtPeticion = dtPeticion?.fromDateTimeStringToLong() ?: 0,
        dtRespuesta = dtRespuesta?.fromDateTimeStringToLong() ?: 0,
        estado = estado.toStringIfNull("null").lowercase(),
        turnoUsuarioPide = turnoUsuarioPide ?: "-",
        turnoUsuarioRecibe = turnoUsuarioRecibe ?: "-",
    )
}

private fun ResponseCuDetallesDTO.asDatabaseModel(): CuDetalle {
    val formattedFecha = fecha.fromDateStringToLong()
    val formattedUpdated = updated.fromDateTimeStringToLong()
    return CuDetalle(
        idDetalle = idDetalle.toLong(),
        idUsuario = idUsuario.toLong(),
        fecha = formattedFecha,
        diaSemana = diaSemana,
        turno = turno ?: "",
        tipo = tipo,
        notas = notas,
        nombreDebe = nombreDebe,
        updated = formattedUpdated,
        libra = libra?.toInt(),
        comj = comj?.toInt(),
        mermas = mermas?.fromTimeWOSecsStringToInt(),
        excesos = excesos?.fromTimeWOSecsStringToInt(),
        excesosGrafico = 0,//Estos se actualizan al pedir excesosGrafico.
    )
}

private fun ResponseCuHistorialDTO.asDatabaseModel(): CuHistorial {
    return CuHistorial(
        id = id.toLong(),
        idDetalle = idDetalle.toLong(),
        turno = turno,
        tipo = tipo,
        nombreDebe = nombreDebe,
        updated = updated.fromDateTimeStringToLong() ?: 0L
    )
}

private fun ResponseEquivalenciasDTO.asDatabaseModel(): GrEquivalencias {
    return GrEquivalencias(
        idGrafico = idGrafico.toLong(), turno = turno, equivalencia = equivalencia
    )
}

private fun ResponseNotasTurnoDTO.asDatabaseModel(): GrNotasTurno {
    return GrNotasTurno(
        id = id.toLong(),
        idGrafico = idGrafico.toLong(),
        turno = turno,
        lunes = lunes.toMyBoolean(),
        martes = martes.toMyBoolean(),
        miercoles = miercoles.toMyBoolean(),
        jueves = jueves.toMyBoolean(),
        viernes = viernes.toMyBoolean(),
        sabado = sabado.toMyBoolean(),
        domingo = domingo.toMyBoolean(),
        festivo = festivo.toMyBoolean(),
        nota = nota
    )
}

private fun ResponseNotasTrenDTO.asDatabaseModel(): GrNotasTren {
    return GrNotasTren(
        id = id.toLong(),
        idGrafico = idGrafico.toLong(),
        tren = tren,
        lunes = lunes.toMyBoolean(),
        martes = martes.toMyBoolean(),
        miercoles = miercoles.toMyBoolean(),
        jueves = jueves.toMyBoolean(),
        viernes = viernes.toMyBoolean(),
        sabado = sabado.toMyBoolean(),
        domingo = domingo.toMyBoolean(),
        festivo = festivo.toMyBoolean(),
        nota = nota
    )
}

private fun ResponseGrTareasDTO.asDatabaseModel(): GrTareas {
    val formattedHoraOrigen = horaOrigen.fromTimeStringToInt()
    val formattedHoraFin = horaFin.fromTimeStringToInt()
    val formattedInserted = inserted.fromDateTimeStringToLong()
    return GrTareas(
        id = idDetalleLibreta.toLong(),
        idGrafico = idGrafico.toLong(),
        turno = turno,
        ordenServicio = ordenServicio.toInt(),
        servicio = servicio,
        tipoServicio = tipoServicio,
        diaSemana = diaSemana,
        sitioOrigen = sitioOrigen,
        horaOrigen = formattedHoraOrigen,
        sitioFin = sitioFin,
        horaFin = formattedHoraFin,
        vehiculo = vehiculo,
        observaciones = observaciones,
        inserted = formattedInserted
    )
}

private fun ResponseExcelIfDTO.asDatabaseModel(): GrExcelIF {
    val formattedHoraOrigen = this.horaOrigen.fromTimeStringToInt()
    val formattedHoraFin = this.horaFin.fromTimeStringToInt()

    return GrExcelIF(
        id = idDetalleGrafico.toLong(),
        idGrafico = idGrafico.toLong(),
        numeroTurno = numeroTurno.toInt(),
        ordenTarea = ordenTarea.toInt(),
        lunes = lunes.toMyBoolean(),
        martes = martes.toMyBoolean(),
        miercoles = miercoles.toMyBoolean(),
        jueves = jueves.toMyBoolean(),
        viernes = viernes.toMyBoolean(),
        sabado = sabado.toMyBoolean(),
        domingo = domingo.toMyBoolean(),
        festivo = festivo.toMyBoolean(),
        comentarioAlTurno = comentarioAlTurno,
        turnoReal = turnoReal,
        sitioOrigen = sitioOrigen,
        horaOrigen = formattedHoraOrigen,
        sitioFin = sitioFin,
        horaFin = formattedHoraFin
    )
}
