package es.kirito.kirito.turnos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.kirito.kirito.core.data.constants.FlagLogout
import es.kirito.kirito.core.data.dataStore.preferenciasKirito
import es.kirito.kirito.core.data.dataStore.updatePreferenciasKirito
import es.kirito.kirito.core.data.database.CuHistorial
import es.kirito.kirito.core.data.database.GrTareas
import es.kirito.kirito.core.data.database.Localizador
import es.kirito.kirito.core.data.database.OtColoresTrenes
import es.kirito.kirito.core.domain.CoreRepository
import es.kirito.kirito.core.domain.models.CuDetalleConFestivoDBModel
import es.kirito.kirito.core.domain.models.GrTareaConClima
import es.kirito.kirito.core.domain.models.TurnoPrxTr
import es.kirito.kirito.core.domain.util.deAyerAHoy
import es.kirito.kirito.core.domain.util.deMananaAHoy
import es.kirito.kirito.core.domain.util.esTipoValido
import es.kirito.kirito.core.domain.util.esTurnoDeTrabajo
import es.kirito.kirito.core.domain.util.isNotNullNorBlank
import es.kirito.kirito.core.domain.util.minuteTimerFlow
import es.kirito.kirito.core.domain.util.pasaPorMedianoche
import es.kirito.kirito.core.domain.util.servicio
import es.kirito.kirito.core.domain.util.toEpochSecondsZoned
import es.kirito.kirito.core.domain.util.toLocalDate
import es.kirito.kirito.core.domain.util.toLocalTime
import es.kirito.kirito.turnos.domain.HoyState
import es.kirito.kirito.turnos.domain.TurnosRepository
import es.kirito.kirito.turnos.domain.models.CuadroAnualVacio
import es.kirito.kirito.turnos.domain.models.ErroresHoy
import es.kirito.kirito.turnos.domain.models.NavigationDestination
import es.kirito.kirito.turnos.domain.models.NavigationObject
import es.kirito.kirito.turnos.domain.models.TeleindicadoresDeTren
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.cuadro_subido_correctamente
import kirito.composeapp.generated.resources.descargando_grafico
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
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
import org.jetbrains.compose.resources.StringResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalCoroutinesApi::class)
class HoyViewModel : ViewModel(), KoinComponent {

    private val repository: TurnosRepository by inject()
    private val coreRepo: CoreRepository by inject()

    private val date: MutableStateFlow<LocalDate?> =
        MutableStateFlow(Clock.System.todayIn(TimeZone.currentSystemDefault()))

    val toastString = MutableSharedFlow<String?>()
    val toastId = MutableSharedFlow<StringResource?>()
    val showToastComjYLibra = MutableSharedFlow<Boolean>()

    private val minuteTimer = minuteTimerFlow()

    private val cuDetalleDelTurno = date.flatMapLatest { date ->
        repository.getCuDetallesDeUnDia(date?.toEpochDays())
    }
    private val turnoPrxTr = date.flatMapLatest { date ->
        repository.getTurnoDeUnDia(date?.toEpochDays())
    }
    private val coloresTrenes = repository.getAllColoresTrenes()

    private val festivo = date.flatMapLatest { date ->
        repository.getFestivoDeUnDia(date?.toEpochDays()).map { it ?: "" }
    }

    //Este advierte de que estás en un festivo, es diferente al resto de toasts.
    val toastFestivo = festivo.map { it.isNotNullNorBlank() }
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    private val showLocalizadorDialog = MutableStateFlow(false)
    private val showImagenEgaDialog = MutableStateFlow(false)
    private val imagenEgaInitialScroll = MutableStateFlow(0)


    private val tareasCortas = turnoPrxTr.flatMapLatest { turno ->
        repository.getTareasCortasDeUnTurno(turno?.idGrafico, turno?.turno, turno?.diaSemana)
    }

    private val tareas = turnoPrxTr.flatMapLatest { turno ->
        repository.getTareasDeUnTurnoDM(turno?.idGrafico, turno?.turno, turno?.diaSemana)
            .combine(minuteTimer) { list, minuteTimer ->
                //   }
                //  .map { list ->
                list.map { tarea ->
                    val climaO = repository.getOneClimaRounded(
                        time =
                        LocalDateTime(
                            (date.value ?: Clock.System.todayIn(TimeZone.currentSystemDefault())),
                            (tarea.horaOrigen ?: 0).toLocalTime()
                        ).toEpochSecondsZoned(),
                        station = tarea.sitioOrigen ?: ""
                    ).firstOrNull()
                    val climaF = repository.getOneClimaRounded(
                        time = LocalDateTime(
                            (date.value ?: Clock.System.todayIn(TimeZone.currentSystemDefault())),
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
                            (date.value ?: Clock.System.todayIn(TimeZone.currentSystemDefault())),
                            (turno?.horaOrigen ?: 0).toLocalTime()
                        ).toEpochSecondsZoned(),
                        tickMinute = minuteTimer
                    )
                }
            }
    }


    private val teleindicadores = turnoPrxTr.flatMapLatest { turno ->
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

    private val notasUsuario = cuDetalleDelTurno.map {
        //TODO: Parse HTML
        // Html.fromHtml(it?.notas ?: "", Html.FROM_HTML_MODE_COMPACT).trim()
        it?.notas.toString()

    }

    private val notasTurno = turnoPrxTr.flatMapLatest {
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

    private val advMermasTurnosLaterales = combine(
        date,
        turnoPrxTr,
        mostrarDescansoEntreTurnos
    ) { fecha, tHoy, mostrarDescansoEntreTurnos ->
        val tAyer = repository.getTurnoDeUnDia(fecha?.minus(1, DateTimeUnit.DAY)?.toEpochDays())
            .firstOrNull()
        val tManana = repository.getTurnoDeUnDia(fecha?.plus(1, DateTimeUnit.DAY)?.toEpochDays())
            .firstOrNull()
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

    private val historial = cuDetalleDelTurno.flatMapLatest { turno ->
        repository.getHistorialDeUnTurno(turno?.idDetalle)
    }

    private val iHaveShiftsShared = preferenciasKirito.map { it.userId }.flatMapLatest { id ->
        repository.tengoLosCambiosActivados(id)
    }

    private val _navigationDestination = MutableStateFlow(
        NavigationObject(NavigationDestination.Nowhere, -1)
    )

    //TODO: Manejar la navegación.
    val navigationDestination = _navigationDestination.asStateFlow()


    private val hayTareas = tareas.map { it.isNotEmpty() }
    private val hayGraficoEnVigor = date
        .flatMapLatest { repository.getGraficoDeUnDia(it?.toEpochDays()?.toLong()) }
        .map { it != null }
    private val elTurnoTieneTareasEnOtrasVariaciones = turnoPrxTr.flatMapLatest { turno ->
        repository.getTareasDeUnTurno(turno?.idGrafico, turno?.turno)
    }
    private val elTurnoEstaEnElGrafico = turnoPrxTr.flatMapLatest { turno ->
        repository.elTurnoTieneExcelIF(turno?.turno, turno?.idGrafico)
    }
    private val combined = combine(
        elTurnoTieneTareasEnOtrasVariaciones,
        elTurnoEstaEnElGrafico
    ) { elTurnoTieneTareasEnOtrasVariaciones, elTurnoEstaEnElGrafico ->
        Pair(elTurnoTieneTareasEnOtrasVariaciones, elTurnoEstaEnElGrafico)
    }

    private val erroresHoy = combine(
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

    private val localizador = cuDetalleDelTurno.flatMapLatest { turno ->
        repository.getOneLocalizador(
            turno = turno?.turno ?: "",
            date = turno?.fecha ?: 0L
        )
    }

    private val novedadesVersion = 5

    val novedades = preferenciasKirito.map {
        it.novedades != novedadesVersion
    }

    val hoyState = combine(
        date, cuDetalleDelTurno, turnoPrxTr, coloresTrenes,
        festivo, tareasCortas, tareas, teleindicadores, notasUsuario,
        notasTurno, advMermasTurnosLaterales, historial, iHaveShiftsShared,
        erroresHoy, localizador, showLocalizadorDialog, showImagenEgaDialog,
        imagenEgaInitialScroll
    ) { array ->
        HoyState(
            array[0] as LocalDate?,
            array[1] as CuDetalleConFestivoDBModel?,
            array[2] as TurnoPrxTr?,
            array[3] as List<OtColoresTrenes>,
            array[4] as String,
            array[5] as List<GrTareas>,
            array[6] as List<GrTareaConClima>,
            array[7] as List<TeleindicadoresDeTren>,
            array[8] as String,
            array[9] as String,
            array[10] as AdvmermasTurnosLaterales,
            array[11] as List<CuHistorial>,
            array[12] as Boolean,
            array[13] as ErroresHoy,
            array[14] as Localizador?,
            array[15] as Boolean,
            array[16] as Boolean,
            array[17] as Int,
        )
    }.distinctUntilChanged()


    fun onSearchClick(tarea: GrTareaConClima) {
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
            date.value?.toEpochDays()?.toLong() ?: return
        )
    }

    fun onExcessClick() {
        _navigationDestination.value = NavigationObject(
            NavigationDestination.Excess,
            date.value?.toEpochDays()?.toLong() ?: return
        )
    }

    fun onEditClick() {
        _navigationDestination.value = NavigationObject(
            NavigationDestination.Edit,
            date.value?.toEpochDays()?.toLong() ?: return
        )
    }

    fun onExchangeClick() {
        _navigationDestination.value = NavigationObject(
            NavigationDestination.Exchange,
            date.value?.toEpochDays()?.toLong() ?: return
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
        date.value = date.value?.minus(days, DateTimeUnit.DAY)
    }

    fun onNextDayClick(days: Int) {
        date.value = date.value?.plus(days, DateTimeUnit.DAY)
    }

    fun onDateSelected(date: LocalDate) {
        this.date.value = date
    }


    fun onComjYLibraClick() {
        viewModelScope.launch(Dispatchers.IO) {
            showToastComjYLibra.emit(true)
        }
    }

    //TODO: Manejar cuando venimos de otro fragmento con una fecha concreta.
    fun setFechaFromOtherFragments(fecha: String?) {
        val epochDay = fecha?.toLongOrNull() ?: return
        date.value = epochDay.toLocalDate()
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
            repository.getIdGraficoDeUnDia(fecha!!).firstOrNull()//Miramos qué gráfico hay este día.
        if (idGrafico != null // && hayInternet()//TODO: Añadir comprobación de internet.
        ) {
            //Si tenemos gráfico asignado...

            if (repository.getOneGrTareasFromGrafico(idGrafico).first() == null) {
                println("No tengo idGrafico")
                try {
                    toastId.emit(Res.string.descargando_grafico)
                    coreRepo.descargarComplementosDelGrafico(listOf(idGrafico.toString()))
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
            date.value?.toEpochDays()?.toLong() ?: return
        )

    }

    //Directamente genero un cuadro con claves vacías y actualizo la bd.
    fun onGenerarCuadroVacioClick() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.requestSubirCuadroVacio(
                    CuadroAnualVacio(
                        year = date.value?.year
                            ?: Clock.System.todayIn(TimeZone.currentSystemDefault()).year,
                        sobrescribir = false
                    )
                )
                coreRepo.descargarCuadroAnual()
                toastId.emit(Res.string.cuadro_subido_correctamente)
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

    fun emitToast() {

    }

    fun emitToast(string: String) {
        viewModelScope.launch {
            toastString.emit(string)
        }
    }

    fun onToastLaunched() {
        //Limpiamos los toasts para que no se vuelvan a emitir.
        //Esta mierda es necesaria, ya que no puedo recolectar los SharedFlows como me gustaría.
        viewModelScope.launch {
            toastString.emit(null)
            toastId.emit(null)
            showToastComjYLibra.emit(false)
        }
    }

    fun onImageEgaClicked(scrollValue: Int, offsetX: Float) {
        val scrolled = scrollValue + offsetX
        val initialScroll = ((scrolled - 200).coerceAtLeast(0F) * 2.5).toInt()
        viewModelScope.launch {
            imagenEgaInitialScroll.value = initialScroll
            showImagenEgaDialog.value = true
        }
    }

    fun onDialogImagenEgaDimissed() {
        viewModelScope.launch {
            showImagenEgaDialog.value = false
        }
    }


    init {
        viewModelScope.launch(Dispatchers.IO) {
            date.collect { fecha ->
                fecha?.let {
                    getCurrentChart(fecha.toEpochDays().toLong())
                }
            }
        }
    }

}

