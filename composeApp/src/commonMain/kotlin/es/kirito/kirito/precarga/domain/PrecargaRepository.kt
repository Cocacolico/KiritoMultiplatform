package es.kirito.kirito.precarga.domain

import androidx.compose.ui.graphics.toArgb
import es.kirito.kirito.core.data.constants.FlagLogout
import es.kirito.kirito.core.data.constants.MyConstants
import es.kirito.kirito.core.data.dataStore.preferenciasKirito
import es.kirito.kirito.core.data.dataStore.updatePreferenciasKirito
import es.kirito.kirito.core.data.database.CaPeticiones
import es.kirito.kirito.core.data.database.Clima
import es.kirito.kirito.core.data.database.ColoresHoraTurnos
import es.kirito.kirito.core.data.database.ConfiguracionAPK
import es.kirito.kirito.core.data.database.CuDetalle
import es.kirito.kirito.core.data.database.CuDiasIniciales
import es.kirito.kirito.core.data.database.CuHistorial
import es.kirito.kirito.core.data.database.Estaciones
import es.kirito.kirito.core.data.database.GrEquivalencias
import es.kirito.kirito.core.data.database.GrExcelIF
import es.kirito.kirito.core.data.database.GrGraficos
import es.kirito.kirito.core.data.database.GrNotasTren
import es.kirito.kirito.core.data.database.GrNotasTurno
import es.kirito.kirito.core.data.database.GrTareas
import es.kirito.kirito.core.data.database.KiritoDatabase
import es.kirito.kirito.core.data.database.Localizador
import es.kirito.kirito.core.data.database.LsUsers
import es.kirito.kirito.core.data.database.OtColoresTrenes
import es.kirito.kirito.core.data.database.OtFestivo
import es.kirito.kirito.core.data.database.OtMensajesAdmin
import es.kirito.kirito.core.data.database.OtTeleindicadores
import es.kirito.kirito.core.data.database.TablonAnuncios
import es.kirito.kirito.core.data.database.TelefonoImportante
import es.kirito.kirito.core.data.database.TurnoCompi
import es.kirito.kirito.core.data.database.UpdatedTables
import es.kirito.kirito.core.data.network.KiritoRequest
import es.kirito.kirito.core.data.network.models.RequestAnioDTO
import es.kirito.kirito.core.data.network.models.RequestAnioUpdatedDTO
import es.kirito.kirito.core.data.network.models.RequestIncluidosDTO
import es.kirito.kirito.core.data.network.models.RequestSimpleDTO
import es.kirito.kirito.core.data.network.models.RequestUpdatedDTO
import es.kirito.kirito.core.domain.CoreRepository
import es.kirito.kirito.core.domain.asDatabaseModel
import es.kirito.kirito.core.domain.kiritoError.lanzarExcepcion
import es.kirito.kirito.core.domain.util.enFormatoDeSalida
import es.kirito.kirito.core.domain.util.fromDateStringToLong
import es.kirito.kirito.core.domain.util.fromDateTimeStdStringToInstant
import es.kirito.kirito.core.domain.util.fromDateTimeStringToLong
import es.kirito.kirito.core.domain.util.fromTimeStringToInt
import es.kirito.kirito.core.domain.util.fromTimeWOSecsStringToInt
import es.kirito.kirito.core.domain.util.normalizeAndRemoveAccents
import es.kirito.kirito.core.domain.util.toInstant
import es.kirito.kirito.core.domain.util.toMyBoolean
import es.kirito.kirito.core.domain.util.toStringIfNull
import es.kirito.kirito.core.presentation.theme.Coral
import es.kirito.kirito.core.presentation.theme.DarkOrchid
import es.kirito.kirito.core.presentation.theme.ForestGreen
import es.kirito.kirito.core.presentation.theme.Gold
import es.kirito.kirito.core.presentation.theme.Maroon
import es.kirito.kirito.core.presentation.theme.RoyalBlue
import es.kirito.kirito.core.presentation.theme.SkyBlue
import es.kirito.kirito.login.data.network.ResponseOtEstacionesDTO
import es.kirito.kirito.precarga.data.network.models.RequestGraficoDTO
import es.kirito.kirito.precarga.data.network.models.RequestStationCoordinatesDTO
import es.kirito.kirito.precarga.data.network.models.RequestTurnosCompiDTO
import es.kirito.kirito.precarga.data.network.models.ResponseCaPeticionesDTO
import es.kirito.kirito.precarga.data.network.models.ResponseColoresTrenesDTO
import es.kirito.kirito.precarga.data.network.models.ResponseCuDetallesDTO
import es.kirito.kirito.precarga.data.network.models.ResponseCuHistorialDTO
import es.kirito.kirito.precarga.data.network.models.ResponseDelAndUpdElementsDTO
import es.kirito.kirito.precarga.data.network.models.ResponseDiasInicialesDTO
import es.kirito.kirito.precarga.data.network.models.ResponseEquivalenciasDTO
import es.kirito.kirito.precarga.data.network.models.ResponseExcelIfDTO
import es.kirito.kirito.precarga.data.network.models.ResponseGrGraficosDTO
import es.kirito.kirito.precarga.data.network.models.ResponseGrTareasDTO
import es.kirito.kirito.precarga.data.network.models.ResponseLocalizadoresDTO
import es.kirito.kirito.precarga.data.network.models.ResponseMensajesAdminDTO
import es.kirito.kirito.precarga.data.network.models.ResponseNotasTrenDTO
import es.kirito.kirito.precarga.data.network.models.ResponseNotasTurnoDTO
import es.kirito.kirito.precarga.data.network.models.ResponseOtFestivosDTO
import es.kirito.kirito.precarga.data.network.models.ResponseOtTablonAnunciosDTO
import es.kirito.kirito.precarga.data.network.models.ResponseTelefonoEmpresaDTO
import es.kirito.kirito.precarga.data.network.models.ResponseTeleindicadorDTO
import es.kirito.kirito.precarga.data.network.models.ResponseTurnoDeCompiDTO
import es.kirito.kirito.precarga.data.network.models.ResponseUserDTO
import es.kirito.kirito.precarga.data.network.models.ResponseWeatherInfoDTO
import es.kirito.kirito.precarga.domain.models.PreloadStep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class PrecargaRepository() : KoinComponent {
    private val database: KiritoDatabase by inject()
    private val coreRepo: CoreRepository by inject()
    private val dao = database.kiritoDao()
    private val ktor = KiritoRequest()

    private val today = Clock.System.todayIn(TimeZone.currentSystemDefault())


    val pasosCompletados = MutableStateFlow(PreloadStep.BEGINNING)


    suspend fun updateKiritoDatabase() {

        //TODO: Desde aquí: Cuando ya sea la 2a y siguientes veces, hay que hacer esto desde un work
        // y desde el equivalente de iOS.
        //En ambos casos hay que hacer un callback o similar para mostrar la sincronización en la barra inferior.
        val bdActualizada = coreRepo.getUpdatedDB().first().toInstant()
        if (bdActualizada.epochSeconds == 0L) {
            //TODO: Ordenar que se muestre el trenecito de la screen.
            setInstallDT()
            //TODO: Clear all tables.
            firstDownloadOfDB(bdActualizada)//Primera vez, bajamos en primer plano.

        } else
            refreshOfDB(bdActualizada)//Esta deberá ser en un futuro con workManager.

    }

    private suspend fun setInstallDT() {
        val installDT = preferenciasKirito.first().installDT
        if (installDT == -1L)
            updatePreferenciasKirito { settings ->
                settings.copy(
                    installDT = Clock.System.now().epochSeconds
                )
            }
    }

    private suspend fun firstDownloadOfDB(bdActualizada: Instant) {
        //TODO: Alarmas.
        //TODO: Solo hacer esto si hay internet. Devolver error si no.
        //Innecesarios porque solo se ejecuta la primera vez.
        //kiritoRepository.processUpdatedElements()
        //kiritoRepository.deleteOldElements()
        updatePasosCompletados(PreloadStep.FESTIVOS)
        refreshOtFestivos(bdActualizada)
        updatePasosCompletados(PreloadStep.GRAFICOS)
        refreshGraficos(bdActualizada)
        updatePasosCompletados(PreloadStep.TURNOS)
        coreRepo.refreshCuDetalles(bdActualizada)
        refreshExcesosGrafico()//Solo en esta primera vez.
        updatePasosCompletados(PreloadStep.MENSAJES_ADMIN)
        refreshMensajesAdmin(bdActualizada)
        updatePasosCompletados(PreloadStep.ELEMENTOS_GENERALES)
        refreshColoresTrenes()
        coreRepo.refreshCambios(bdActualizada)
        refreshTelefonosDeEmpresa(bdActualizada)
        refreshTablonAnuncios(bdActualizada)
        refreshDiasIniciales(
            Clock.System.now().toLocalDateTime(TimeZone.UTC).year
        )//Solo en la primera vez.
        updatePasosCompletados(PreloadStep.TELEINDICADORES)
        refreshTeleindicadores()
        //y solo bajar cuando sea necesario.
        updatePasosCompletados(PreloadStep.TURNOS_COMPIS)
        refreshUsuarios(bdActualizada)
        updatePasosCompletados(PreloadStep.ESTACIONES)
        refreshEstaciones()
        updatePasosCompletados(PreloadStep.GRAFICO_ACTUAL)
        /** EXCLUSIVO EN LA PRECARGA INICIAL, NO SE USA REITERATIVAMENTE. **/
        insertFirstColorHoraTurnos()
        //  kiritoRepository.firstTimeAlarm(getApplication<Application>().applicationContext)

        refreshRecentGraficos(bdActualizada)

        dao.getMyUserPermisoTurnos(preferenciasKirito.first().userId.toString()).first()
            .let { muestroCuadros ->
                if (muestroCuadros == 1) {
                    updatePasosCompletados(PreloadStep.TURNOS_COMPIS)
                    refreshTurnosCompis(bdActualizada)
                }
            }
        //TODO: Esto ajusta el automatismo que cada día comprueba gráficos
        // automáticamente. Recuerda que lanza notificaciones cuando se acerca un gráfico.
        //CheckGraficos().startCheckGraficosWork(workManager)
        updatePasosCompletados(PreloadStep.INFO_METEOROLOGICA)
        refreshWeatherInformation()
        refreshLocalizadores(bdActualizada)

        //TODO: Esto enciende el automatismo que cada 27h sincroniza la bd si no se ha hecho antes.
        //programarPreCargaWorker()


        saveUpdatedDB()
        updatePasosCompletados(PreloadStep.FINISHED) //Nos vamos

    }


    private suspend fun refreshOfDB(bdActualizada: Instant) {

        updateElementosGlobales(bdActualizada)
        // checkVersionAge()
     //   updatePasosCompletados(PreloadStep.GRAFICOS)
        // refreshGraficos(bdActualizada)
        updatePasosCompletados(PreloadStep.GRAFICO_ACTUAL)
        refreshRecentGraficos(bdActualizada)
     //   updatePasosCompletados(PreloadStep.ELEMENTOS_VIEJOS)
        //  processUpdatedElements(bdActualizada)
        // deleteOldElements()
    //    updatePasosCompletados(PreloadStep.TURNOS)
        // coreRepo.refreshCuDetalles(bdActualizada)
    //    updatePasosCompletados(PreloadStep.FESTIVOS)
        //   refreshOtFestivos(bdActualizada)
     //   updatePasosCompletados(PreloadStep.MENSAJES_ADMIN)
        //    refreshMensajesAdmin(bdActualizada)
   //     updatePasosCompletados(PreloadStep.ELEMENTOS_GENERALES)
        //    coreRepo.refreshCambios(bdActualizada)
        //    refreshTablonAnuncios(bdActualizada)
        //   refreshTelefonosDeEmpresa(bdActualizada)

    //    updatePasosCompletados(PreloadStep.USUARIOS)
        //   refreshUsuarios(bdActualizada)
        //TODO: Hacerlo cuando tengamos alarmas
        //   refreshAlarmas(applicationContext)
        updatePasosCompletados(PreloadStep.TURNOS_COMPIS)

        dao.getMyUserPermisoTurnos(preferenciasKirito.first().userId.toString()).first()
            .let { muestroCuadros ->
                if (muestroCuadros == 1) {
                    refreshTurnosCompis(bdActualizada)
                }
            }
        updatePasosCompletados(PreloadStep.INFO_METEOROLOGICA)
        refreshWeatherInformation()
        refreshLocalizadores(bdActualizada)


        //TODO: Meter esto cuando sepamos de workManager y equivalente de ios.
        //CheckGraficos().startCheckGraficosWork(workManager)
        //programarPreCargaWorker()

        /** Guardo el valor de updatedDB. **/
        saveUpdatedDB()
        updatePasosCompletados(PreloadStep.FINISHED)
    }

    private suspend fun updateElementosGlobales(bdActualizada: Instant) {
        RequestUpdatedDTO("refresco_general", bdActualizada.enFormatoDeSalida()).let { salida ->
            ktor.requestUpdateElementosGlobales(salida).respuesta?.let { resp ->
                resp.festivos?.forEach { festivo ->
                    dao.insertOtFestivos(festivo.asDatabaseModel())
                }
                resp.graficos?.forEach { grafico ->
                    dao.upsertGrafico(grafico.asDatabaseModel())
                }
                resp.turnos?.forEach {
                    dao.insertCuDetalles(it.asDatabaseModel())
                }
                resp.historiales?.forEach {
                    dao.insertCuHistorial(it.asDatabaseModel())
                }
                resp.mensajes?.forEach {
                    dao.insertMensajeDeAdmin(it.asDatabaseModel())
                }
                resp.cambios?.forEach {
                    dao.upsertCaPeticiones(it.asDatabaseModel())
                }
                resp.telefonos_importantes?.forEach {
                    dao.insertTelefonoDeEmpresa(it.asDatabaseModel())
                }
                resp.tablones?.forEach {
                    dao.upsertTablonAnuncio(it.asDatabaseModel())
                }
                resp.usuarios?.forEach {
                    dao.insertUsuarios(it.asDatabaseModel())
                }
                resp.actualizados?.let {
                    processUpdatedElements(bdActualizada, resp.actualizados)
                }
                resp.borrados?.let {
                    deleteOldElements(resp.borrados)
                }
            }
        }
    }

    private fun updatePasosCompletados(step: PreloadStep) {
        pasosCompletados.value = step
    }


    private suspend fun refreshOtFestivos(bdActualizada: Instant) {

        val salida = RequestUpdatedDTO("festivos.obtener", bdActualizada.enFormatoDeSalida())
        val respuesta = ktor.requestOtFestivos(salida)

        if (respuesta.error.lanzarExcepcion()) {
            respuesta.respuesta?.forEach {
                dao.insertOtFestivos(it.asDatabaseModel())
            }
        }
    }

    private suspend fun refreshGraficos(bdActualizada: Instant) {
        val salida = RequestUpdatedDTO("graficos.obtener", bdActualizada.enFormatoDeSalida())
        val respuesta = ktor.requestGraficos(salida)

        if (respuesta.error.lanzarExcepcion()) {
            respuesta.respuesta?.forEach {
                dao.upsertGrafico(it.asDatabaseModel())
            }
        }
    }


    private suspend fun refreshExcesosGrafico(
        year: Int = Clock.System.now().toLocalDateTime(TimeZone.UTC).year
    ) {
        val salida = RequestAnioDTO(
            peticion = "turnos.graficos_excesos_auto.obtener",
            anio = year.toString()
        )
        val respuesta = ktor.requestExcesosGrafico(salida)
        if (respuesta.error.lanzarExcepcion()) {
            // Primero limpio lo anterior!
            dao.clearExcesosGrafico(
                LocalDate(year, 1, 1).toEpochDays().toLong(),
                LocalDate(year, 12, 31).toEpochDays().toLong()
            )
            respuesta.respuesta?.forEach {
                dao.updateExcesoGrafico(
                    date = it.fecha.fromDateStringToLong().toInt(),
                    minutos = it.exceso.fromTimeStringToInt()
                )
            }
        }
    }

    private suspend fun refreshMensajesAdmin(bdActualizada: Instant) {
        bdActualizada.enFormatoDeSalida().let { updatedString ->
            RequestUpdatedDTO("mensajes.obtener", updatedString).let { salida ->
                val respuesta = ktor.requestMensajesAdmin(salida)
                if (respuesta.error.lanzarExcepcion()) {
                    respuesta.respuesta?.forEach {
                        dao.insertMensajeDeAdmin(it.asDatabaseModel())
                    }
                }
            }
        }
    }

    private suspend fun refreshColoresTrenes() {
        RequestSimpleDTO("otros.obtener_colores_trenes").let { salida ->
            val respuesta = ktor.requestColoresTrenes(salida)
            if (respuesta.error.lanzarExcepcion()) {
                respuesta.respuesta?.forEach {
                    dao.insertColoresTrenes(it.asDatabaseModel())
                }
            }
        }
    }


    private suspend fun refreshTelefonosDeEmpresa(bdActualizada: Instant) {
        bdActualizada.enFormatoDeSalida().let { updatedString ->
            RequestUpdatedDTO("telefonos_importantes.obtener", updatedString).let { salida ->
                val respuesta = ktor.requestTelefonosEmpresa(salida)
                if (respuesta.error.lanzarExcepcion()) {
                    respuesta.respuesta?.forEach {
                        dao.upsertTelefonoDeEmpresa(it.asDatabaseModel())
                    }
                }
            }
        }
    }

    private suspend fun refreshTablonAnuncios(bdActualizada: Instant) {
        deleteOldTablonAnuncios()
        bdActualizada.enFormatoDeSalida().let { updatedString ->
            RequestUpdatedDTO("cambios_tablon.obtener", updatedString).let { salida ->
                val respuesta = ktor.requestOtTablonAnuncios(salida)
                if (respuesta.error.lanzarExcepcion()) {
                    respuesta.respuesta?.forEach {
                        dao.upsertTablonAnuncio(it.asDatabaseModel())
                    }
                }
            }
        }
    }

    private suspend fun deleteOldTablonAnuncios() {
        Clock.System.now().epochSeconds.let { hoy ->
            dao.deleteOldTablonAnuncios(hoy)
        }
    }

    private suspend fun refreshDiasIniciales(year: Int) {
        RequestAnioDTO("dias_iniciales.obtener", year.toString()).let { salida ->
            val respuesta = ktor.requestDiasIniciales(salida)
            if (respuesta.error.lanzarExcepcion()) {
                respuesta.respuesta?.forEach {
                    dao.insertDiasIniciales(it.asDatabaseModel())
                }
            }
        }
    }

    private suspend fun refreshTeleindicadores() {
        RequestSimpleDTO("teleindicadores.obtener").let { salida ->
            val respuesta = ktor.requestTeleindicadores(salida)
            if (respuesta.error.lanzarExcepcion()) {
                respuesta.respuesta?.forEach {
                    dao.insertTeleindicador(it.asDatabaseModel())
                }
            }
        }
    }

    private suspend fun refreshUsuarios(bdActualizada: Instant) {
        RequestUpdatedDTO(
            peticion = "usuarios.listado",
            updated = bdActualizada.enFormatoDeSalida()
        ).let { salida ->
            val respuesta = ktor.requestUsuarios(salida)
            if (respuesta.error.lanzarExcepcion()) {
                val veoTurnosCompis = dao.getMyUserPermisoTurnos(
                    preferenciasKirito.map { it.userId }.first().toString()
                ).first()
                respuesta.respuesta?.forEach { usuario ->
                    //Si vienen los usuarios "fantasma", no hay que meterlos en la lista de usuarios.
                    if (usuario.id?.toLongOrNull() != null) {
                        val usuarioAntes = dao.getCompi(usuario.id!!.toLong()).first()
                        dao.insertUsuarios(usuario.asDatabaseModel())
                        //Ahora miramos si ha cambiado a 1 el mostrar cuadros, y nos lo bajamos entonces.
                        if (veoTurnosCompis == 1 && usuarioAntes != null && usuarioAntes.mostrarCuadros != "1" && usuario.mostrarCuadros == "1") {
                            refreshTurnosDeUnCompi(usuario.id!!.toLong())
                        }
                        if (usuario.mostrarCuadros != "1") {
                            dao.deleteTurnosCompisOfUser(usuario.id?.toLongOrNull())
                        }
                    }
                }
            }
        }
    }

    private suspend fun refreshTurnosDeUnCompi(compi: Long) {
        val thisYear = Clock.System.now().toLocalDateTime(TimeZone.UTC).year
        val lista = dao.getTableUpdatedYears(MyConstants.TABLE_TURNOS_COMPIS).first()
        val listaMutable = lista.toMutableList()
        listaMutable.add(thisYear)
        listaMutable.distinct().forEach { year ->
            refreshTurnosDeUnCompi(year, compi)
        }
    }

    private suspend fun refreshTurnosDeUnCompi(year: Int, compi: Long) {
        RequestTurnosCompiDTO(
            peticion = "turnos.turnos_compis.obtener",
            anio = year.toString(),
            id_compi = compi.toString()
        ).let { salida ->
            val respuesta = ktor.requestTurnosDeUnCompi(salida)
            if (respuesta.error.lanzarExcepcion()) {
                respuesta.respuesta?.forEach {
                    dao.insertTurnoCompi(it.asDatabaseModel())
                }
            }
        }
    }

    private suspend fun refreshEstaciones() {
        val hayEstaciones = dao.hayEstaciones().first()
        if (!hayEstaciones) {
            RequestSimpleDTO("otros.obtener_estaciones").let { salida ->
                val respuesta = ktor.requestOtEstaciones(salida)
                if (respuesta.error.lanzarExcepcion()) {
                    respuesta.respuesta?.forEach {
                        dao.insertEstacion(it.asDatabaseModel())
                    }

                }
            }
        }
    }

    private suspend fun insertFirstColorHoraTurnos() {

        listOf(
            ColoresHoraTurnos(
                0,
                DarkOrchid.toArgb(),
                LocalTime(3, 0).toSecondOfDay().toLong()
            ),
            ColoresHoraTurnos(
                0,
                RoyalBlue.toArgb(),
                LocalTime(6, 0).toSecondOfDay().toLong()
            ),
            ColoresHoraTurnos(
                0,
                SkyBlue.toArgb(),
                LocalTime(8, 30).toSecondOfDay().toLong()
            ),
            ColoresHoraTurnos(
                0,
                ForestGreen.toArgb(),
                LocalTime(11, 0).toSecondOfDay().toLong()
            ),
            ColoresHoraTurnos(
                0,
                Gold.toArgb(),
                LocalTime(15, 0).toSecondOfDay().toLong()
            ),
            ColoresHoraTurnos(
                0,
                Coral.toArgb(),
                LocalTime(18, 0).toSecondOfDay().toLong()
            ),
            ColoresHoraTurnos(
                0,
                Maroon.toArgb(),
                LocalTime(20, 30).toSecondOfDay().toLong()
            ),
            ColoresHoraTurnos(
                0,
                DarkOrchid.toArgb(),
                LocalTime(23, 59).toSecondOfDay().toLong()
            )
        ).forEach {
            dao.insertColoresHoraTurnos(it)
        }
    }

    private suspend fun refreshRecentGraficos(bdActualizada: Instant) {
        dao.getGraficosDeSeisMeses(today.toEpochDays().toLong())
            .map { lista ->
                lista.filter {
                    //DEJA ESTOS ELEMENTOS: los que sean MÁS NUEVOS O DE 1970.
                    it.fechaUltimoCambio == null ||
                            bdActualizada.epochSeconds == 0L ||//O todos si la bd está sin actualizar.
                            it.fechaUltimoCambio.toInstant() > bdActualizada
                }.filter { grafico ->
                    //Este segundo filtro es para no hacer tantas queries, que tardan.
                    dao.graficoTieneExcelIF(grafico.idGrafico).first().let { yaDescargado ->
                        (grafico.fechaUltimoCambio == null && !yaDescargado || grafico.fechaUltimoCambio != null)
                        //Me bajo los que tengan fecha null y no se hayan bajado y el resto.
                    }
                }
            }.map { lista -> lista.map { grafico -> grafico.idGrafico.toString() } }
            .first().let { idGraficos ->
                if (idGraficos.isNotEmpty())
                    coreRepo.descargarComplementosDelGrafico(idGraficos)
            }
    }


    private suspend fun saveUpdatedDB() {
        val now = Clock.System.now()
        dao.insertInUpdatedTables(
            UpdatedTables(
                MyConstants.GENERAL_UPLOAD,
                0,
                now.epochSeconds
            )
        )
    }

    private suspend fun refreshTurnosCompis(bdActualizada: Instant) {
        val thisYear = Clock.System.todayIn(TimeZone.currentSystemDefault()).year
        val lista = dao.getTableUpdatedYears(MyConstants.TABLE_TURNOS_COMPIS).first()
            .plus(thisYear)
        lista.distinct().forEach { year ->
            refreshTurnosCompis(year, bdActualizada)
        }
    }

    private suspend fun refreshTurnosCompis(year: Int, bdActualizada: Instant) {
        RequestAnioUpdatedDTO(
            "turnos.turnos_compis.obtener",
            year.toString(),
            bdActualizada.enFormatoDeSalida()
        ).let { salida ->
            val respuesta = ktor.requestTurnosCompis(salida)
            if (respuesta.error.lanzarExcepcion()) {
                respuesta.respuesta?.forEach {
                    dao.insertTurnoCompi(it.asDatabaseModel())
                }
            }
        }
    }

    private suspend fun refreshWeatherInformation() {
        dao.deleteOldClima(today.minus(1, DateTimeUnit.DAY).toEpochDays().toLong())
        checkChartStationsWOCoordinates()
        refreshCoordinatesFromStations()
        updateWeatherFromChartStations()

    }

    private suspend fun checkChartStationsWOCoordinates() {
        dao.getEstacionesEnGraficos().first()
            .filterNot { it.isBlank() }
            .forEach { nombreEstacion ->
                val exists = dao.isStationInEstaciones(nombreEstacion).first()
                if (!exists) {
                    //La estación no existe, la metemos.
                    dao.insertEstacion(
                        Estaciones(
                            nombreEstacion,
                            acronimo = "xx",
                            numero = "xxxxx",
                            longitud = null,
                            latitud = null,
                            esDelGrafico = true
                        )
                    )
                }
            }
        dao.getEstacionesDeGraficos().map { lista ->
            lista.filterNot { estacion ->
                estacion.esDelGrafico
            }
        }.first()
            .forEach { estacion ->
                dao.upsertEstacion(
                    estacion.copy(esDelGrafico = true)
                )
            }
    }

    private suspend fun refreshCoordinatesFromStations() {

        val stations = getChartStationsWithoutCoordinates().first()

        stations.forEach { estacion ->
            val salida = RequestStationCoordinatesDTO(
                "otros.googlemaps",
                buildStationName(estacion.nombre)
            )
            val respuesta = ktor.requestStationCoordinates(salida)

            if (respuesta.respuesta?.status == "REQUEST_DENIED") {//Hay un problema mayor.
                println("Request denied al pedir unas coordenadas")
            } else {
                val estacionWCoordinates =
                    if (respuesta.respuesta?.status == "ZERO_RESULTS") {
                        println("Request de residencia ha dado 0 results")
                        estacion.copy(latitud = 0f, longitud = 0f)
                    } else
                        estacion.copy(
                            latitud = respuesta.respuesta?.results?.getOrNull(0)?.geometry?.location?.lat?.toFloat()
                                ?: 0f,
                            longitud = respuesta.respuesta?.results?.getOrNull(0)?.geometry?.location?.lng?.toFloat()
                                ?: 0f
                        )
                println("Vamos a actualizar la estación $estacionWCoordinates")
                dao.upsertEstacion(estacionWCoordinates)
            }
        }
    }

    private fun buildStationName(nombre: String): String {
        val NOMBRE = nombre.uppercase()
        var texto =
            if (NOMBRE.contains("CERRO NEGRO"))
                "Madrid Atocha"
            else if (NOMBRE.contains("CAN TUNIS"))
                "Hospitalet de Llobregat"
            else
                nombre

        texto = texto.replace(Regex("[(AV)]"), "")

        texto =
            texto.plus(" Spain")//Esta coletilla hay que meterla para que vaya bien en la mayoría de los sitios.
        return texto
    }

    private fun getChartStationsWithoutCoordinates(): Flow<List<Estaciones>> {
        return dao.getChartStationsWithoutCoordinates()
    }

    private suspend fun updateWeatherFromChartStations() {
        val updated = preferenciasKirito.map { it.weatherUpdated }.first()
        if (updated != 0L &&
            !areMoreThanSixHoursApart(updated.toInstant(), Clock.System.now())
        )
            return
        val estaciones = dao.getEstacionesDeGraficos().first().filterNot {
            it.longitud == null || it.longitud == 0f
        }
        estaciones.forEach { estacion ->
            buildWeatherEndpoint(estacion).let { salida ->
                val respuesta = ktor.requestWeatherInfo(salida)

                val climas = respuesta.asClimas(estacion)
                climas.forEach { clima ->
                    dao.upsertClima(clima)
                }
            }
        }
        updatePreferenciasKirito {
            it.copy(
                weatherUpdated = Clock.System.now().epochSeconds
            )
        }
    }

    private fun buildWeatherEndpoint(estacion: Estaciones): String {
        return "https://api.open-meteo.com/v1/forecast?latitude=${estacion.latitud}&longitude=${estacion.longitud}&hourly=temperature_2m,precipitation_probability,rain,snowfall,cloud_cover,visibility,wind_speed_10m&forecast_days=4"
    }

    private fun areMoreThanSixHoursApart(updated: Instant, now: Instant): Boolean {
        val duration = now - updated
        return duration.inWholeHours > 6
    }

    private suspend fun refreshLocalizadores(bdActualizada: Instant) {
        dao.deleteOldLocalizador(today.toEpochDays().toLong())
        RequestUpdatedDTO(
            "localizadores.obtener",
            bdActualizada.enFormatoDeSalida()
        ).let { salida ->
            val respuesta = ktor.requestLocalizadores(salida)
            if (respuesta.error.lanzarExcepcion()) {
                respuesta.respuesta?.forEach {
                    dao.upsertLocalizador(it.asDatabaseModel())
                }
            }
        }
    }

    private suspend fun checkVersionAge() {
        RequestSimpleDTO("otros.version").let { salida ->
            val respuesta = ktor.requestSimpleEmptyResponse(salida)
            val error = respuesta.error.errorCode
            if (error == "10001")
                setOldVersionFlag()
            if (error == "2")
                setWrongTokenFlag()
            if (error == "6")
                setMustUpdApp()
            if (error == "0")
                setCorrectVersionApp()
        }
    }

    /** Desde aquí levantamos un flag que luego se consulta en muchos sitios de la app y fuerza que nos salgamos. **/
    private suspend fun setWrongTokenFlag() {
        val flag = ConfiguracionAPK(MyConstants.FLAG_LOGOUT, FlagLogout.WRONG_TOKEN)
        dao.setLogoutFlag(flag)
    }

    private suspend fun setMustUpdApp() {
        val flag = ConfiguracionAPK(MyConstants.FLAG_LOGOUT, FlagLogout.MUST_UPD)
        dao.setLogoutFlag(flag)
    }

    private suspend fun setCorrectVersionApp() {
        val flag = ConfiguracionAPK(MyConstants.FLAG_LOGOUT, FlagLogout.NOT_NEEDED)
        dao.setLogoutFlag(flag)
    }

    private suspend fun setOldVersionFlag() {
        val flag = ConfiguracionAPK(MyConstants.FLAG_LOGOUT, FlagLogout.SHOULD_UPD)
        dao.setLogoutFlag(flag)
    }

    private suspend fun processUpdatedElements(bdActualizada: Instant) {
        RequestSimpleDTO("otros.obtener_elementos_actualizados").let { salida ->
            val respuesta = ktor.requestDelAndUpdElements(salida)
            if (respuesta.error.lanzarExcepcion()) {
               respuesta.respuesta?.toMutableList()?.filterNot { elemento ->
                        elemento.included.fromDateTimeStringToLong()!! < bdActualizada.epochSeconds
                    }?.let { respMapeada ->
                   for (item in respMapeada) {
                       when (item.tabla) {
                           "OT_colores_trenes" -> updateColoresTrenes()
                           "OT_estaciones" -> updateEstaciones()
                           "OT_teleindicadores" -> updateTeleindicadores()
                           "NO_mensajes" -> updateMensajesAdmin(item.idElemento, bdActualizada)
                           else -> Unit
                       }
                   }
                }
            }
        }
    }

    private suspend fun processUpdatedElements(
        bdActualizada: Instant,
        elements: List<ResponseDelAndUpdElementsDTO>
    ) {
        val respMapeada = elements.toMutableList().filterNot { elemento ->
            elemento.included.fromDateTimeStringToLong()!! < bdActualizada.epochSeconds
        }
        for (item in respMapeada) {
            when (item.tabla) {
                "OT_colores_trenes" -> updateColoresTrenes()
                "OT_estaciones" -> updateEstaciones()
                "OT_teleindicadores" -> updateTeleindicadores()
                "NO_mensajes" -> updateMensajesAdmin(item.idElemento, bdActualizada)
                else -> Unit
            }
        }
    }

    private suspend fun updateMensajesAdmin(idElemento: String?, bdActualizada: Instant) {
        refreshMensajesAdmin(bdActualizada)
    }

    private suspend fun updateTeleindicadores() {
        dao.deleteAllTeleindicadores()
        refreshTeleindicadores()
    }

    private suspend fun updateEstaciones() {
        RequestIncluidosDTO("otros.obtener_estaciones", "1").let { salida ->
            val respuesta = ktor.requestOtEstacionesInc(salida)
            if (respuesta.error.lanzarExcepcion()) {
                respuesta.respuesta?.forEach {
                    dao.insertEstacion(it.asDatabaseModel())
                }
            }
        }
    }

    private suspend fun updateColoresTrenes() {
        dao.deleteAllColoresTrenes()
        refreshColoresTrenes()
    }

    private suspend fun deleteOldElements() {
        RequestSimpleDTO("otros.obtener_elementos_borrados").let { salida ->
            val respuesta = ktor.requestDelAndUpdElements(salida)
            if (respuesta.error.lanzarExcepcion()) {

                respuesta.respuesta?.forEach { elemento ->
                    when (elemento.tabla) {
                        "OT_festivos" -> dao.deletedElementFestivo(elemento.idElemento?.toLongOrNull())
                        "OT_telefonos_residencia" -> Unit
                        "LS_users" -> deleteUserAndComplements(elemento.idElemento?.toLongOrNull())
                        "GR_listado_graficos" -> deleteGraficoAndComplements(elemento.idElemento?.toLongOrNull())
                        "NO_mensajes" -> deleteMensajeDeAdminLocally(elemento.idElemento?.toLongOrNull())
                        "CA_anuncios" -> dao.deleteAnuncio(elemento.idElemento?.toLongOrNull())
                        else -> Unit
                    }
                }
            }
        }
    }

    private suspend fun deleteOldElements(elements: List<ResponseDelAndUpdElementsDTO>) {
        elements.forEach { elemento ->
            when (elemento.tabla) {
                "OT_festivos" -> dao.deletedElementFestivo(elemento.idElemento?.toLongOrNull())
                "OT_telefonos_residencia" -> Unit
                "LS_users" -> deleteUserAndComplements(elemento.idElemento?.toLongOrNull())
                "GR_listado_graficos" -> deleteGraficoAndComplements(elemento.idElemento?.toLongOrNull())
                "NO_mensajes" -> deleteMensajeDeAdminLocally(elemento.idElemento?.toLongOrNull())
                "CA_anuncios" -> dao.deleteAnuncio(elemento.idElemento?.toLongOrNull())
                else -> Unit
            }
        }
    }

    private suspend fun deleteUserAndComplements(idUser: Long?) {
        dao.deleteTurnosCompisOfUser(idUser)
        dao.deletedElementUsuario(idUser)
    }

    private suspend fun deleteMensajeDeAdminLocally(id: Long?) {
        dao.deleteMensajeDeAdmin(id)
    }


    private suspend fun deleteGraficoAndComplements(idGrafico: Long?) {
        dao.deleteGrNotasTurnoDelGrafico(idGrafico ?: -1L)
        dao.deleteGrNotasTrenDelGrafico(idGrafico ?: -1L)
        dao.deleteGrExcelIF(idGrafico)
        dao.deleteAGrTareas(idGrafico)
        dao.deletedElementGrafico(idGrafico)
    }


}


private fun ResponseLocalizadoresDTO.asDatabaseModel(): Localizador {
    return Localizador(
        fecha = fecha?.fromDateStringToLong() ?: 1L,
        turno = turno.toString(),
        localizador = localizador.toString()
    )
}

private fun ResponseWeatherInfoDTO.asClimas(estacion: Estaciones): List<Clima> {
    val salida = mutableListOf<Clima>()
    this.hourly?.time?.forEachIndexed { index, time ->
        val clima = Clima(
            time = time.fromDateTimeStdStringToInstant().epochSeconds,
            estacion = estacion.nombre,
            temperatura = this.hourly?.temperature2m?.get(index) ?: -100f,
            probabilidadLluvia = this.hourly?.precipitationProbability?.get(index) ?: -100,
            lluvia = this.hourly?.rain?.get(index) ?: -100f,
            nieve = this.hourly?.snowfall?.get(index) ?: -100f,
            nublado = this.hourly?.cloudCover?.get(index) ?: -100,
            created = Clock.System.now().epochSeconds,
            viento = this.hourly?.windSpeed?.get(index) ?: -1f,
            visibilidad = this.hourly?.visibility?.get(index)?.toInt() ?: 1,
        )
        salida.add(clima)
    }
    return salida
}

private fun ResponseOtEstacionesDTO.asDatabaseModel(): Estaciones {
    return Estaciones(
        nombre = nombre,
        acronimo = acronimo,
        numero = numero,
        longitud = null,
        latitud = null,
    )
}

private fun ResponseTurnoDeCompiDTO.asDatabaseModel(): TurnoCompi {
    return TurnoCompi(
        idDetalle = id_detalle.toLong(),
        idUsuario = id_usuario.toLong(),
        fecha = fecha.fromDateStringToLong(),
        turno = turno,
        tipo = tipo,
    )
}

private fun ResponseUserDTO.asDatabaseModel(): LsUsers {
    return LsUsers(
        id = this.id!!.toLong(),
        username = this.username.toString(),
        email = this.email.toString(),
        name = this.name.toString(),
        normalizedName = this.name.toString().normalizeAndRemoveAccents(),
        normalizedSurname = this.surname.toString().normalizeAndRemoveAccents(),
        surname = this.surname.toString(),
        workPhoneExt = this.workPhoneExt.toString(),
        workPhone = this.workPhone.toString(),
        personalPhone = this.personalPhone.toString(),
        mostrarTelfPersonal = this.mostrarTelfPersonal.toString(),
        mostrarTelfTrabajo = this.mostrarTelfTrabajo.toString(),
        mostrarCuadros = this.mostrarCuadros.toString(),
        mostrarCuadrosCuando = this.mostrarCuadrosCuando.toString(),
        admin = this.admin.toString(),
        cambiosActivados = this.cambiosActivados.toString(),
        cambiosActivadosCuando = this.cambiosActivadosCuando?.fromDateTimeStringToLong(),
        comentariosAlAdmin = this.comentariosAlAdmin.toString(),
        recibirEmailNotificaciones = this.recibirEmailNotificaciones.toString(),
        keyAccessWeb = this.keyAccessWeb.toString(),
        keyIcs = this.keyIcs.toString(),
        notas = this.notas.toString(),
        lastLogin = this.lastLogin.toString().fromDateTimeStringToLong(),
        peticionesDiarias = this.peticionesDiarias.toString(),
        photo = this.photo.toString(),
        created = this.created.toString().fromDateTimeStringToLong(),
        disabled = this.disabled.toString()
    )
}

private fun ResponseTeleindicadorDTO.asDatabaseModel(): OtTeleindicadores {
    return OtTeleindicadores(
        //    id = id.toLong(),
        id = 0L,//Se autogeneran
        tren = tren,
        lunes = lunes.toMyBoolean(),
        martes = martes.toMyBoolean(),
        miercoles = miercoles.toMyBoolean(),
        jueves = jueves.toMyBoolean(),
        viernes = viernes.toMyBoolean(),
        sabado = sabado.toMyBoolean(),
        domingo = domingo.toMyBoolean(),
        festivo = festivo.toMyBoolean(),
        notas = notas,
        codigo = codigo,
        vehiculo = vehiculo
    )
}

private fun ResponseDiasInicialesDTO.asDatabaseModel(): CuDiasIniciales {
    return CuDiasIniciales(
        anio = anio.toInt(),
        tipo = tipo,
        valor = valorInicial.toInt()
    )
}

private fun ResponseOtTablonAnunciosDTO.asDatabaseModel(): TablonAnuncios {
    return TablonAnuncios(
        id = id.toLongOrNull() ?: 0,
        idUsuario = idUsuario.toLongOrNull() ?: 0,
        fecha = fecha.fromDateStringToLong(),
        titulo = titulo.toStringIfNull(""),
        explicacion = explicacion.toStringIfNull(""),
        etiqueta1 = etiqueta1,
        etiqueta2 = etiqueta2,
        etiqueta3 = etiqueta3,
        updated = updated?.fromDateTimeStringToLong()
    )
}

private fun ResponseTelefonoEmpresaDTO.asDatabaseModel(): TelefonoImportante {
    return TelefonoImportante(
        id = id.toLong(),
        empresa = empresa,
        tipoServicio = tipoServicio,
        nombre = nombre,
        telefono1 = telefono1.toLongOrNull() ?: 0L,
        telefono2 = telefono2.toLongOrNull() ?: 0L
    )
}

private fun ResponseColoresTrenesDTO.asDatabaseModel(): OtColoresTrenes {
    return OtColoresTrenes(filtro, color)
}

private fun ResponseMensajesAdminDTO.asDatabaseModel(): OtMensajesAdmin {
    return OtMensajesAdmin(
        id = this.id?.toLongOrNull() ?: 0,
        titulo = this.titulo ?: "",
        mensaje = this.mensaje ?: "",
        enviado = this.enviado?.fromDateTimeStringToLong() ?: 0L,
        enviadoPor = this.enviadoPor ?: "",
        estado = this.estado?.toIntOrNull() ?: 0,
    )
}

private fun ResponseGrGraficosDTO.asDatabaseModel(): GrGraficos {
    val formattedFechaInicio = this.fechaInicio.toString().fromDateStringToLong()
    val formattedFechaFinal = this.fechaFinal.toString().fromDateStringToLong()
    val formattedFechaUltimoCambio = this.fechaUltimoCambio.toString().fromDateTimeStringToLong()
    return GrGraficos(
        idGrafico = this.idGrafico.toLong(),
        fechaInicio = formattedFechaInicio,
        fechaFinal = formattedFechaFinal,
        descripcion = this.descripcion,
        ficheroGrafico = this.nombreFicheroGrafico,
        ficheroAnexo = this.nombreFicheroAnexo,
        enlaceGraficoGdrive = this.enlaceGDriveFicheroGrafico,
        enlaceAnexoGdrive = this.enlaceGDriveFicheroAnexo,
        nombreGraficoGdrive = this.nombreGDriveFicheroGrafico,
        nombreAnexoGdrive = this.nombreGDriveFicheroAnexo,
        fechaUltimoCambio = formattedFechaUltimoCambio
    )
}

private fun ResponseOtFestivosDTO.asDatabaseModel(): OtFestivo {
    val formattedFecha = this.fecha.fromDateStringToLong()
    val cosa = OtFestivo(
        fecha = formattedFecha,
        idFestivo = this.idFestivo?.toLong() ?: 0,
        descripcion = this.descripcion
    )

    return cosa
}



