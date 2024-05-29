package es.kirito.kirito.turnos.presentation

import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.kirito.kirito.core.data.constants.FlagLogout
import es.kirito.kirito.core.data.dataStore.preferenciasKirito
import es.kirito.kirito.core.data.dataStore.updatePreferenciasKirito
import es.kirito.kirito.core.domain.CoreRepository
import es.kirito.kirito.core.domain.models.GrTarea
import es.kirito.kirito.core.domain.models.TurnoPrxTr
import es.kirito.kirito.core.domain.util.deAyerAHoy
import es.kirito.kirito.core.domain.util.deMananaAHoy
import es.kirito.kirito.core.domain.util.esTipoValido
import es.kirito.kirito.core.domain.util.esTurnoDeTrabajo
import es.kirito.kirito.core.domain.util.fraseDescansoAntes
import es.kirito.kirito.core.domain.util.fraseDescansoDespues
import es.kirito.kirito.core.domain.util.isNotNullNorBlank
import es.kirito.kirito.core.domain.util.minuteTimerFlow
import es.kirito.kirito.core.domain.util.pasaPorMedianoche
import es.kirito.kirito.core.domain.util.servicio
import es.kirito.kirito.core.domain.util.toEpochSecondsZoned
import es.kirito.kirito.core.domain.util.toLocalDate
import es.kirito.kirito.core.domain.util.toLocalTime
import es.kirito.kirito.login.domain.LoginRepository
import es.kirito.kirito.precarga.domain.PrecargaRepository
import es.kirito.kirito.turnos.domain.TurnosRepository
import es.kirito.kirito.turnos.domain.models.CuadroAnualVacio
import es.kirito.kirito.turnos.domain.models.ErroresHoy
import es.kirito.kirito.turnos.domain.models.NavigationDestination
import es.kirito.kirito.turnos.domain.models.NavigationObject
import es.kirito.kirito.turnos.domain.models.TeleindicadoresDeTren
import es.kirito.kirito.turnos.domain.utils.genComjYLibraString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(ExperimentalCoroutinesApi::class)
class HoyViewModel: ViewModel(), KoinComponent {

    private val repository: TurnosRepository by inject()
    private val coreRepo: CoreRepository by inject()
    private val precargaRepo: PrecargaRepository by inject()



    private val _date: MutableStateFlow<LocalDate?> =
        MutableStateFlow(Clock.System.todayIn(TimeZone.currentSystemDefault()))
    val date = _date.asStateFlow()

    val toastString = MutableSharedFlow<String?>()


    val minuteTimer = minuteTimerFlow()


    val cuDetalleDelTurno = _date.flatMapLatest { date ->
        repository.getCuDetallesDeUnDia(date?.toEpochDays())
    }
    val turnoPrxTr = _date.flatMapLatest { date ->
        repository.getTurnoDeUnDia(date?.toEpochDays())
    }
    val coloresTrenes = repository.getAllColoresTrenes()

    val festivo = _date.flatMapLatest { date ->
        repository.getFestivoDeUnDia(date?.toEpochDays()).map { it ?: "" }
    }

    val showLocalizadorDialog = MutableStateFlow(false)


    val toastFestivo = festivo.map { it.isNotNullNorBlank() }
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    val tareasCortas = turnoPrxTr.flatMapLatest { turno ->
        repository.getTareasCortasDeUnTurno(turno?.idGrafico, turno?.turno, turno?.diaSemana)
    }

    //TODO: Imagenes ega
//    val imagenEgaDialog = MutableStateFlow(DialogImageEga())
//
//    val imagenEga =
//        combine(
//            turnoPrxTr,
//            tareasCortas,
//            coloresTrenes,
//            _date,
//            minuteTimer
//        ) { turno, tareas, colores, date, minuteTick ->
//            if (turno?.hora_origen == null)
//                return@combine null
//            else
//                TurnoParaImagen(
//                    timeInicio = turno.hora_origen ?: 0,
//                    timeFin = turno.hora_fin ?: 0,
//                    tareas = tareas,
//                    dia = date?.toEpochDay(),
//                    zoom = 2,
//                    coloresTrenes = colores,
//                    minuteTick = minuteTick
//                )
//        }



    val tareas = turnoPrxTr.flatMapLatest { turno ->
        repository.getTareasDeUnTurnoDM(turno?.idGrafico, turno?.turno, turno?.diaSemana)
            .combine(minuteTimer) { list, minuteTimer ->
                //   }
                //  .map { list ->
                list.map { tarea ->
                    val climaO = repository.getOneClimaRounded(
                        time =
                        LocalDateTime(
                            (_date.value ?: Clock.System.todayIn(TimeZone.currentSystemDefault())),
                            (tarea.horaOrigen ?: 0).toLocalTime()
                        ).toEpochSecondsZoned(),
                        station = tarea.sitioOrigen ?: ""
                    ).firstOrNull()
                    val climaF = repository.getOneClimaRounded(
                        time = LocalDateTime(
                            (_date.value ?: Clock.System.todayIn(TimeZone.currentSystemDefault())),
                            (tarea.horaFin ?: 0).toLocalTime()
                        ).toEpochSecondsZoned(),
                        station = tarea.sitioFin ?: ""
                    ).firstOrNull()
                    tarea.copy(
                        tempO = climaO?.temperatura,
                        probO = climaO?.probabilidadLluvia,
                        lluviaO = climaO?.lluvia,
                        nieveO = climaO?.nieve,
                        nubladoO = climaO?.nublado,
                        vientoO = climaO?.viento,
                        visibilidadO = climaO?.visibilidad,
                        tempF = climaF?.temperatura,
                        probF = climaF?.probabilidadLluvia,
                        lluviaF = climaF?.lluvia,
                        nieveF = climaF?.nieve,
                        nubladoF = climaF?.nublado,
                        vientoF = climaF?.viento,
                        visibilidadF = climaF?.visibilidad,
                        //Inserted lo usamos para ver la hora de inicio y colorear las tareas si ya han empezado.
                        inserted = LocalDateTime(
                            (_date.value ?: Clock.System.todayIn(TimeZone.currentSystemDefault())),
                            (turno?.horaOrigen ?: 0).toLocalTime()
                        ).toEpochSecondsZoned(),
                        tickMinute = minuteTimer
                    )
                }
            }
    }


    val teleindicadores = turnoPrxTr.flatMapLatest { turno ->
        repository.getTeleindicadores(turno?.idGrafico, turno?.turno, turno?.diaSemana)
            .map { teleindicadores ->
                teleindicadores.filterNot { teleindicador ->
                    teleindicador.codigo.isBlank() || teleindicador.vehiculo.isBlank()
                }
            }
            .map { teleindicadores ->
                val vehiculos =
                    teleindicadores.map { it.vehiculo }.distinct().filter { it.isNotNullNorBlank() }
                val trenes =
                    teleindicadores.map { it.tren }.distinct().filter { it.isNotNullNorBlank() }
                val teleindicadoresDeTren: MutableList<TeleindicadoresDeTren> = mutableListOf()
                var stringLength = 6

                teleindicadoresDeTren.add(
                    TeleindicadoresDeTren(
                        tren = "",//La primera esquina la dejamos en blanco!
                        teleindicadores = vehiculos, //Lo que serán las cabeceras, los vehículos.
                        stringLength = 0,
                    )
                )

                trenes.forEach { tren ->
                    val indicadores: MutableList<String> = mutableListOf()
                    vehiculos.forEach { vehiculo ->
                        val indicadoresString =
                            teleindicadores.filter { it.vehiculo == vehiculo && it.tren == tren }
                                .joinToString { it.codigo }
                        stringLength =
                            indicadoresString.length.coerceAtLeast(stringLength) //Lo aumentamos, si es mayor.
                        indicadores.add(
                            indicadoresString
                        )
                    }
                    teleindicadoresDeTren.add(
                        TeleindicadoresDeTren(
                            tren = tren,
                            teleindicadores = indicadores,
                            stringLength = 0,
                        )
                    )
                }
                teleindicadoresDeTren
                    .map { indicador ->
                        indicador.stringLength = stringLength
                    }
                teleindicadoresDeTren.toList()

            }


    }

    val notasUsuario = cuDetalleDelTurno.map {
        //TODO: Parse HTML
       // Html.fromHtml(it?.notas ?: "", Html.FROM_HTML_MODE_COMPACT).trim()
        it?.notas.toString()

    }

    val notasTurno = turnoPrxTr.flatMapLatest {
        repository.getNotasDelTurno(it?.idGrafico, it?.turno, it?.diaSemana)
            .map { lista ->
                val concatenated = lista.joinToString(separator = "") { nota -> nota.nota }
                //TODO: Parse HTML
               // Html.fromHtml(concatenated, Html.FROM_HTML_MODE_COMPACT).trim()
                concatenated
            }
    }

    data class AdvmermasTurnosLaterales(
        val show: Boolean,
        val tAyer: TurnoPrxTr? = null,
        val tHoy: TurnoPrxTr? = null,
        val tManana: TurnoPrxTr? = null,
    )

    private val mostrarDescansoEntreTurnos =
        repository.configTiempoEntreTurnos //0 siempre visible, 1 a veces, 2 nunca.

    val advMermasTurnosLaterales = combine(
        _date,
        turnoPrxTr,
        mostrarDescansoEntreTurnos
    ) { fecha, tHoy, mostrarDescansoEntreTurnos ->
        val tAyer = repository.getTurnoDeUnDia(fecha?.minus(1, DateTimeUnit.DAY)?.toEpochDays()).firstOrNull()
        val tManana = repository.getTurnoDeUnDia(fecha?.plus(1, DateTimeUnit.DAY)?.toEpochDays()).firstOrNull()
        val respuestaVacia =
            AdvmermasTurnosLaterales(show = false)
        if (tHoy == null)
            return@combine respuestaVacia

        val hayMermas: Boolean =
            (tAyer?.deAyerAHoy(tHoy.horaOrigen) ?: 99999) in 0..50400
                    || (tManana?.deMananaAHoy(tHoy.horaFin, tHoy.pasaPorMedianoche())
                ?: 99999) in 0..50400

        val fraseAdvertencia = AdvmermasTurnosLaterales(
            show = true,
            tAyer = tAyer,
            tHoy = tHoy,
            tManana = tManana
        )

        return@combine when (mostrarDescansoEntreTurnos) {
            0 -> fraseAdvertencia
            1 -> {
                if (hayMermas)
                    fraseAdvertencia
                else
                    respuestaVacia
            }

            else -> respuestaVacia
        }
    }

    val historial = cuDetalleDelTurno.flatMapLatest { turno ->
        repository.getHistorialDeUnTurno(turno?.idDetalle)
    }

    val iHaveShiftsShared = preferenciasKirito.map { it.userId }.flatMapLatest { id ->
        repository.tengoLosCambiosActivados(id)
    }

    private val _navigationDestination = MutableStateFlow(
        NavigationObject(NavigationDestination.Nowhere, -1)
    )
    //TODO: Manejar la navegación.
    val navigationDestination = _navigationDestination.asStateFlow()


    val hayTareas = tareas.map { it.isNotEmpty() }
    val hayGraficoEnVigor = _date
        .flatMapLatest { repository.getGraficoDeUnDia(it?.toEpochDays()?.toLong()) }
        .map { it != null }
    val elTurnoTieneTareasEnOtrasVariaciones = turnoPrxTr.flatMapLatest { turno ->
        repository.getTareasDeUnTurno(turno?.idGrafico, turno?.turno)
    }
    val elTurnoEstaEnElGrafico = turnoPrxTr.flatMapLatest { turno ->
        repository.elTurnoTieneExcelIF(turno?.turno, turno?.idGrafico)
    }
    val combined = combine(
        elTurnoTieneTareasEnOtrasVariaciones,
        elTurnoEstaEnElGrafico
    ) { elTurnoTieneTareasEnOtrasVariaciones, elTurnoEstaEnElGrafico ->
        Pair(elTurnoTieneTareasEnOtrasVariaciones, elTurnoEstaEnElGrafico)
    }

    val erroresHoy = combine(
        cuDetalleDelTurno,
        turnoPrxTr,
        hayTareas,
        hayGraficoEnVigor,
        combined,
    ) { cuDetalle, prxTr, hayTareas, hayGraficoEnVigor, combined ->
        val elTurnoTieneTareasEnOtrasVariaciones = combined.first
        val elTurnoEstaEnElGrafico = combined.second

        var errores = ErroresHoy(
            hayErrores = false,
            noHayCuDetalle = false,
            noHayGraficoEnVigor = false,
            noEstaEnElDiaQueToca = false,
            elTurnoEsUnTipo = false,
            elTurnoNoTieneTareas = false,
            elTurnoNoEstaEnElGrafico = false
        )
        if (cuDetalle == null)
            errores = errores.copy(hayErrores = true, noHayCuDetalle = true)
        if (!hayGraficoEnVigor)
            errores = errores.copy(hayErrores = true, noHayGraficoEnVigor = true)
        if (!hayTareas && elTurnoTieneTareasEnOtrasVariaciones)
            errores = errores.copy(hayErrores = true, noEstaEnElDiaQueToca = true)

        if (!elTurnoTieneTareasEnOtrasVariaciones && prxTr?.esTurnoDeTrabajo() == true) {
            if (prxTr.turno.esTipoValido())
                errores = errores.copy(hayErrores = true, elTurnoEsUnTipo = true)
            else if (elTurnoEstaEnElGrafico)
                errores = errores.copy(hayErrores = true, elTurnoNoTieneTareas = true)
            else if (prxTr.tipo != "7000")
                errores = errores.copy(hayErrores = true, elTurnoNoEstaEnElGrafico = true)
        }

        errores
    }


    fun onSearchClick(tarea: GrTarea) {
        _navigationDestination.value = NavigationObject(
            destination = NavigationDestination.Search,
            date = -1,
            idGrafico = tarea.idGrafico,
            diaSemana = tarea.diaSemana ?: "",
            tren = tarea.servicio(),
        )
    }

    fun onBulkEditClick() {
        _navigationDestination.value = NavigationObject(
            NavigationDestination.BulkEdit,
            _date.value?.toEpochDays()?.toLong() ?: return
        )
    }

    fun onExcessClick() {
        _navigationDestination.value = NavigationObject(
            NavigationDestination.Excess,
            _date.value?.toEpochDays()?.toLong() ?: return
        )
    }

    fun onEditClick() {
        _navigationDestination.value = NavigationObject(
            NavigationDestination.Edit,
            _date.value?.toEpochDays()?.toLong() ?: return
        )
    }

    fun onExchangeClick() {
        _navigationDestination.value = NavigationObject(
            NavigationDestination.Exchange,
            _date.value?.toEpochDays()?.toLong() ?: return
        )
    }

    fun onNavigated() {
        if (_navigationDestination.value.destination != NavigationDestination.Nowhere)
            _navigationDestination.value = NavigationObject(
                NavigationDestination.Nowhere,
                -1
            )
    }


    fun onPreviousDayClick(days: Int) {
        _date.value = _date.value?.minus(days,DateTimeUnit.DAY)
    }

    fun onNextDayClick(days: Int) {
        _date.value = _date.value?.plus(days,DateTimeUnit.DAY)
    }

    fun onDateSelected(date: LocalDate) {
        _date.value = date
    }


    fun onComjYLibraClick() {
        viewModelScope.launch(Dispatchers.IO) {
            //TODO: Mostrar este toast correctamente.
//            val turno = cuDetalleDelTurno.firstOrNull()
//            val frase = turno?.genComjYLibraString()
//            if (frase != null) {
//                toastString.emit(frase)
//            }
        }
    }

    val localizador = cuDetalleDelTurno.flatMapLatest { turno ->
        repository.getOneLocalizador(
            turno = turno?.turno ?: "",
            date = turno?.fecha ?: 0L
        )
    }

    //TODO: IMAGEN EGA
//    fun onImageClicked(scroll: Int, offset: Float, turno: TurnoParaImagen, context: Context) {
//        imagenEgaDialog.value = DialogImageEga(turno.genImage(context, 5), scroll, offset)
//    }

    fun setFechaFromOtherFragments(fecha: String?) {
        val epochDay = fecha?.toLongOrNull() ?: return
        _date.value = epochDay.toLocalDate()
    }

    private val novedadesVersion = 5

    val novedades = preferenciasKirito.map {
        it.novedades != novedadesVersion
    }

    fun onNovedadShown() {
        viewModelScope.launch(Dispatchers.IO) {
            updatePreferenciasKirito {
                it.copy(novedades = novedadesVersion)
            }
        }
    }


    /**
     * A PARTIR DE AQUÍ:
     * El trabajo de back end que hace este fragmento, donde se comprueban cosas.
     * */

    //El liveData lo uso en el fragmento para realizar la navegación y para lanzar un toast o un diálogo.
    //No uso el propio compose hasta que toda la navegación sea en compose, porque dará crashes.
    val flagLogout = repository.checkLogoutFlag()
        .map { flag ->
            if (flag == FlagLogout.WRONG_TOKEN || flag == FlagLogout.MUST_UPD)
                withContext(Dispatchers.IO) {
                    coreRepo.nukeAll()
                }
            flag
        }
        .distinctUntilChanged()//Por el motivo que sea, emite como un loco esto.



    //TODO: LLamar a este desde el init o algún sitio bueno.
//    fun sincronizacionGeneralDeDB(newInternet: Boolean = false) {
//        viewModelScope.launch(Dispatchers.IO) {
//            HoyGeneralSyncUseCase().invoke(application, newInternet)
//        }
//    }

    //Elegimos el gráfico del día seleccionado.
    private suspend fun getCurrentChart(fechaEntrada: Long? = null) {
        var fecha = fechaEntrada//fechaEntrada no se usa.
        if (fechaEntrada == null)
            fecha = Clock.System.todayIn(TimeZone.currentSystemDefault()).toEpochDays().toLong()
        val idGrafico =
            repository.getIdGraficoDeUnDia(fecha!!)//Miramos qué gráfico hay este día.
        if (idGrafico != null // && hayInternet()//TODO: Añadir comprobación de internet.
            ) {
            //Si tenemos gráfico asignado...

            if (repository.getOneGrTareasFromGrafico(idGrafico) == null) {
                println("No tengo idGrafico")
                try {
                    //TODO: Informar de que intento bajarme el gráfico.
//                    toastString.emit(
//                        application.applicationContext.getString(
//                            R.string.descargando_grafico
//                        )
//                    )

                    precargaRepo.descargarComplementosDelGrafico(idGrafico)
                } catch (e: Exception) {
                    if (e.message != "Ignorar") {
                        //TODO: Firebase
                       // FirebaseCrashlytics.getInstance().recordException(e)
                        toastString.emit(e.message)
                    }
                }
            }
        } else {
           // Timber.i("Este día no tiene gráfico asignado.")
        }
    }

    fun onGenerarCuadroClick() {
        _navigationDestination.value = NavigationObject(
            NavigationDestination.NewChart,
            _date.value?.toEpochDays()?.toLong() ?: return
        )

    }

    //Directamente genero un cuadro con claves vacías y actualizo la bd.
    fun onGenerarCuadroVacioClick() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.requestSubirCuadroVacio(
                    CuadroAnualVacio(
                        year = _date.value?.year ?: Clock.System.todayIn(TimeZone.currentSystemDefault()).year,
                        sobrescribir = false
                    )
                )
                repository.descargarCuadroAnual()
                //TODO: Informar de este toast.
            //    toastString.emit(application.applicationContext.getString(R.string.cuadro_subido_correctamente))
            } catch (e: Exception) {
                toastString.emit(e.message)
                println(e.message)
            }
        }
    }

    fun onLocalizadorClick() {
        showLocalizadorDialog.value = true
    }

    fun hideLocalizadorDialog() {
        showLocalizadorDialog.value = false
    }


    init {
        viewModelScope.launch(Dispatchers.IO) {
            _date.collect { fecha ->
                fecha?.let {
                    getCurrentChart(fecha.toEpochDays().toLong())
                }
            }
        }
    }

}

