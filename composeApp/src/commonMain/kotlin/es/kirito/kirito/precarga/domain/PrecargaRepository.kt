package es.kirito.kirito.precarga.domain

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


    val pasosCompletados = MutableStateFlow("0")


    suspend fun updateKiritoDatabase() {

        //TODO: Desde aquí: Cuando ya sea la 2a y siguientes veces, hay que hacer esto desde un work
        // y desde el equivalente de iOS.
        //En ambos casos hay que hacer un callback o similar para mostrar la sincronización en la barra inferior.
        val bdActualizada = coreRepo.getUpdatedDB().toInstant()
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
        updatePasosCompletados("2")
        refreshOtFestivos(bdActualizada)
        updatePasosCompletados("3")
        refreshGraficos(bdActualizada)
        updatePasosCompletados("4")
        refreshCuDetalles(bdActualizada)
        refreshExcesosGrafico()//Solo en esta primera vez.
        updatePasosCompletados("5")
        refreshMensajesAdmin(bdActualizada)
        updatePasosCompletados("6")
        refreshColoresTrenes()
        refreshCambios(bdActualizada)
        refreshTelefonosDeEmpresa(bdActualizada)
        refreshTablonAnuncios(bdActualizada)
        refreshDiasIniciales(
            Clock.System.now().toLocalDateTime(TimeZone.UTC).year
        )//Solo en la primera vez.
        updatePasosCompletados("7")
        refreshTeleindicadores()
        //y solo bajar cuando sea necesario.
        updatePasosCompletados("8")
        refreshUsuarios(bdActualizada)
        updatePasosCompletados("9")
        refreshEstaciones()
        updatePasosCompletados("10")
        /** EXCLUSIVO EN LA PRECARGA INICIAL, NO SE USA REITERATIVAMENTE. **/
        insertFirstColorHoraTurnos()
        //  kiritoRepository.firstTimeAlarm(getApplication<Application>().applicationContext)

        refreshRecentGraficos(bdActualizada)

        dao.getMyUserPermisoTurnos(preferenciasKirito.first().userId.toString())
            .let { muestroCuadros ->
                if (muestroCuadros == 1) {
                    updatePasosCompletados("10")
                    refreshTurnosCompis(bdActualizada)
                }
            }
        //TODO: Esto ajusta el automatismo que cada día comprueba gráficos
        // automáticamente. Recuerda que lanza notificaciones cuando se acerca un gráfico.
        //CheckGraficos().startCheckGraficosWork(workManager)
        updatePasosCompletados("11")
        refreshWeatherInformation()
        refreshLocalizadores(bdActualizada)

        //TODO: Esto enciende el automatismo que cada 27h sincroniza la bd si no se ha hecho antes.
        //programarPreCargaWorker()


        saveUpdatedDB()
        updatePasosCompletados("12") //Nos vamos

    }


    private suspend fun refreshOfDB(bdActualizada: Instant) {

        checkVersionAge()
        updatePasosCompletados("1")
        refreshGraficos(bdActualizada)
        updatePasosCompletados("2")
        refreshRecentGraficos(bdActualizada)
        updatePasosCompletados("3")
        processUpdatedElements(bdActualizada)
        deleteOldElements()
        updatePasosCompletados("4")
        refreshCuDetalles(bdActualizada)
        updatePasosCompletados("5")
        refreshOtFestivos(bdActualizada)
        updatePasosCompletados("6")
        refreshMensajesAdmin(bdActualizada)
        updatePasosCompletados("7")
        refreshCambios(bdActualizada)
        refreshTablonAnuncios(bdActualizada)
        refreshTelefonosDeEmpresa(bdActualizada)

        updatePasosCompletados("8")
        refreshUsuarios(bdActualizada)
        //TODO: Hacerlo cuando tengamos alarmas
     //   refreshAlarmas(applicationContext)
        updatePasosCompletados("9")

        dao.getMyUserPermisoTurnos(preferenciasKirito.first().userId.toString())
            .let { muestroCuadros ->
                if (muestroCuadros == 1) {
                    updatePasosCompletados("10")
                    refreshTurnosCompis(bdActualizada)
                }
            }
        updatePasosCompletados("11")
        refreshWeatherInformation()
        refreshLocalizadores(bdActualizada)


        //TODO: Meter esto cuando sepamos de workManager y equivalente de ios.
        //CheckGraficos().startCheckGraficosWork(workManager)
        //programarPreCargaWorker()

        /** Guardo el valor de updatedDB. **/
        saveUpdatedDB()
        updatePasosCompletados("12")
    }

    private fun updatePasosCompletados(step: String) {
        pasosCompletados.value = step
    }


    private suspend fun refreshOtFestivos(bdActualizada: Instant) {

        val salida = RequestUpdatedDTO("festivos.obtener", bdActualizada.enFormatoDeSalida())
        val respuesta = ktor.requestOtFestivos(salida)

        if (respuesta.error.errorCode == "0" && respuesta.respuesta != null) {
            respuesta.respuesta.forEach {
                dao.insertOtFestivos(it.asDatabaseModel())
            }
        }
    }

    private suspend fun refreshGraficos(bdActualizada: Instant) {
        val salida = RequestUpdatedDTO("graficos.obtener", bdActualizada.enFormatoDeSalida())
        val respuesta = ktor.requestGraficos(salida)

        if (respuesta.error.errorCode == "0") {
            respuesta.respuesta?.forEach {
                dao.upsertGrafico(it.asDatabaseModel())
            }
        }
    }

    private suspend fun refreshCuDetalles(bdActualizada: Instant) {
        refreshComplementosTurnos(bdActualizada)
        val salida = RequestUpdatedDTO("turnos.obtener", bdActualizada.enFormatoDeSalida())
        val respuesta = ktor.requestCuDetalles(salida)
        if (respuesta.error.errorCode == "0")
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
                if (respuesta.error.errorCode == "0") {
                    respuesta.respuesta?.forEach {
                        dao.insertCuHistorial(it.asDatabaseModel())
                    }
                }
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
        if (respuesta.error.errorCode == "0") {
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
                if (respuesta.error.errorCode == "0") {
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
            if (respuesta.error.errorCode == "0") {
                respuesta.respuesta?.forEach {
                    dao.insertColoresTrenes(it.asDatabaseModel())
                }
            }
        }
    }

    private suspend fun refreshCambios(bdActualizada: Instant) {
        bdActualizada.enFormatoDeSalida().let { updatedString ->
            RequestUpdatedDTO("cambios.obtener", updatedString).let { salida ->
                val respuesta = ktor.requestCaPeticiones(salida)
                if (respuesta.error.errorCode == "0") {
                    respuesta.respuesta?.forEach {
                        dao.upsertCaPeticiones(it.asDatabaseModel())
                    }
                }
            }
        }
    }

    private suspend fun refreshTelefonosDeEmpresa(bdActualizada: Instant) {
        bdActualizada.enFormatoDeSalida().let { updatedString ->
            RequestUpdatedDTO("telefonos_importantes.obtener", updatedString).let { salida ->
                val respuesta = ktor.requestTelefonosEmpresa(salida)
                if (respuesta.error.errorCode == "0") {
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
                if (respuesta.error.errorCode == "0") {
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
            if (respuesta.error.errorCode == "0") {
                respuesta.respuesta?.forEach {
                    dao.insertDiasIniciales(it.asDatabaseModel())
                }
            }
        }
    }

    private suspend fun refreshTeleindicadores() {
        RequestSimpleDTO("teleindicadores.obtener").let { salida ->
            val respuesta = ktor.requestTeleindicadores(salida)
            if (respuesta.error.errorCode == "0") {
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
            if (respuesta.error.errorCode == "0") {
                val veoTurnosCompis = dao.getMyUserPermisoTurnos(
                    preferenciasKirito.map { it.userId }.first().toString()
                )
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
        val lista = dao.getTableUpdatedYears(MyConstants.TABLE_TURNOS_COMPIS)
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
            if (respuesta.error.errorCode == "0") {
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
                if (respuesta.error.errorCode == "0") {
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
                DarkOrchid.value.toInt(),
                LocalTime(3, 0).toSecondOfDay().toLong()
            ),
            ColoresHoraTurnos(
                0,
                RoyalBlue.value.toInt(),
                LocalTime(6, 0).toSecondOfDay().toLong()
            ),
            ColoresHoraTurnos(
                0,
                SkyBlue.value.toInt(),
                LocalTime(8, 30).toSecondOfDay().toLong()
            ),
            ColoresHoraTurnos(
                0,
                ForestGreen.value.toInt(),
                LocalTime(11, 0).toSecondOfDay().toLong()
            ),
            ColoresHoraTurnos(
                0,
                Gold.value.toInt(),
                LocalTime(15, 0).toSecondOfDay().toLong()
            ),
            ColoresHoraTurnos(
                0,
                Coral.value.toInt(),
                LocalTime(18, 0).toSecondOfDay().toLong()
            ),
            ColoresHoraTurnos(
                0,
                Maroon.value.toInt(),
                LocalTime(20, 30).toSecondOfDay().toLong()
            ),
            ColoresHoraTurnos(
                0,
                DarkOrchid.value.toInt(),
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
                }
            }.first().forEach { grafico ->
                dao.graficoTieneExcelIF(grafico.idGrafico).let { yaDescargado ->
                    if (grafico.fechaUltimoCambio == null && !yaDescargado || grafico.fechaUltimoCambio != null)
                    //Me bajo los que tengan fecha null y no se hayan bajado y el resto.
                        descargarComplementosDelGrafico(grafico.idGrafico)
                }
            }
    }

    private suspend fun descargarComplementosDelGrafico(idGrafico: Long) {
        refreshGrExcelIF(idGrafico)
        refreshGrTareas(idGrafico)
        refreshNotasTren(idGrafico)
        refreshNotasTurno(idGrafico)
        refreshEquivalencias(idGrafico)
    }

    private suspend fun refreshGrExcelIF(idGrafico: Long) {
        RequestGraficoDTO("excelif.obtener", idGrafico.toString()).let { salida ->
            val respuesta = ktor.requestExcelIf(salida)
            if (respuesta.error.errorCode == "0") {
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
            if (respuesta.error.errorCode == "0") {
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
            if (respuesta.error.errorCode == "0") {
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
            if (respuesta.error.errorCode == "0") {
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
            if (respuesta.error.errorCode == "0") {
                dao.deleteGrEquivalenciasDelGrafico(idGrafico)
                respuesta.respuesta?.forEach {
                    dao.insertGrEquivalencias(it.asDatabaseModel())
                }
            }
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
        val lista = dao.getTableUpdatedYears(MyConstants.TABLE_TURNOS_COMPIS)
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
            if (respuesta.error.errorCode == "0") {
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
                val exists = dao.isStationInEstaciones(nombreEstacion)
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
            if (respuesta.error.errorCode == "0") {
                respuesta.respuesta?.forEach {
                    dao.upsertLocalizador(it.asDatabaseModel())
                }
            }
        }
    }

    private suspend fun checkVersionAge() {
        RequestSimpleDTO("otros.version").let { salida ->
            val respuesta = ktor.requestVersionAge(salida)
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
            if (respuesta.error.errorCode == "0") {
                val respMapeada = respuesta.respuesta?.toMutableList()
                if (respMapeada != null) {
                    respMapeada.filterNot { elemento ->
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
            if (respuesta.error.errorCode == "0") {
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
            if (respuesta.error.errorCode == "0") {

                respuesta.respuesta?.forEach {elemento ->
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



