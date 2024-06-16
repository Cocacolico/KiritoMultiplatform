package es.kirito.kirito.turnos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.kirito.kirito.core.domain.error.IntentNoAppError
import es.kirito.kirito.core.domain.error.Result
import es.kirito.kirito.core.data.database.GrGraficos
import es.kirito.kirito.core.data.database.GrTareas
import es.kirito.kirito.core.data.database.LsUsers
import es.kirito.kirito.core.data.database.OtColoresTrenes
import es.kirito.kirito.core.data.database.OtTeleindicadores
import es.kirito.kirito.core.domain.CoreRepository
import es.kirito.kirito.core.domain.models.GrTareaBuscador
import es.kirito.kirito.core.domain.models.GrTareaConClima
import es.kirito.kirito.core.domain.models.TurnoBuscador
import es.kirito.kirito.core.domain.useCases.dialPhoneNumberUseCase
import es.kirito.kirito.core.domain.useCases.abrirWhatsappUseCase
import es.kirito.kirito.core.domain.util.inicial
import es.kirito.kirito.turnos.domain.BuscadorState
import es.kirito.kirito.turnos.domain.TurnosRepository
import es.kirito.kirito.turnos.domain.models.Buscando
import es.kirito.kirito.turnos.domain.models.OrdenBusqueda
import es.kirito.kirito.turnos.domain.models.TurnoBuscadorDM
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.no_hay_una_aplicaci_n_para_hacer_llamadas
import kirito.composeapp.generated.resources.parece_que_no_tienes_instalado_whatsapp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.StringResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


data class SelectedFilters(
    var idGrafico: Long,
    var diaSemana: String = "todo",
    var orden: OrdenBusqueda = OrdenBusqueda.CLAVE,
)


@Suppress("UNCHECKED_CAST")
class BuscadorViewModel : ViewModel(), KoinComponent {

    private val repository: TurnosRepository by inject()
    private val coreRepo: CoreRepository by inject()


    private val coloresTrenes = repository.getAllColoresTrenes()

    private val _toast: MutableStateFlow<String?> = MutableStateFlow(null)
    val toast = _toast.asStateFlow()
    private val _toastId: MutableStateFlow<StringResource?> = MutableStateFlow(null)
    val toastId = _toastId.asStateFlow()

    private val buscando: MutableStateFlow<Buscando> = MutableStateFlow(Buscando.TURNOS)
    private val dialogCompi: MutableStateFlow<LsUsers> = MutableStateFlow(LsUsers())
    private val imagenEgaInitialScroll = MutableStateFlow(0)
    private val imagenEgaTurno: MutableStateFlow<TurnoBuscadorDM?> = MutableStateFlow(null)
    private val textfieldText = MutableStateFlow("")


    private val hayTeleindicadores = repository.hayTeleindicadores()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), false)
    private val listaGraficos = coreRepo.getGraficosDescargados()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())


    private val idGrafico = MutableStateFlow(-1L)
    private val _turno = MutableStateFlow("todo")
    private val _tren = MutableStateFlow("")
    private val _trenDeTeleind = MutableStateFlow("todo")
    private val diaSemana = MutableStateFlow("todo")


    private val ordenBusqueda = MutableStateFlow(OrdenBusqueda.CLAVE)


    fun applySelectedFilters(selectedFilters: SelectedFilters) {
        //Cuando cambias el orden búsqueda a compi, seleccionamos el día de hoy.
        if (ordenBusqueda.value != selectedFilters.orden && selectedFilters.orden == OrdenBusqueda.COMPANY)
            diaSemana.value =
                Clock.System.todayIn(TimeZone.currentSystemDefault()).dayOfWeek.inicial()
        else
            diaSemana.value = selectedFilters.diaSemana
        idGrafico.value = selectedFilters.idGrafico
        ordenBusqueda.value = selectedFilters.orden

    }

    private val turnosBuscados = combine(
        idGrafico,
        _turno,
        diaSemana,
        ordenBusqueda,
        coloresTrenes
    ) { idGrafico, turno, diaSemana, orden, coloresTrenes ->
        repository.getTurnoBuscado2(idGrafico, turno, diaSemana, orden)
            .map { lista ->
                lista.distinctBy {
                    it.turno to it.diaSemana to it.idGrafico
                }
                    .map { turno ->
                        val estasTareas = repository.getTareasDeUnTurnoBuscador2(
                            turno.idGrafico,
                            turno.turno,
                            turno.diaSemana.aSemanalSingle()
                        ).firstOrNull()
                        val notasTurno = repository.getNotasDelTurno(
                            turno.idGrafico,
                            turno.turno,
                            turno.diaSemana.aSemanalSingle()
                        ).map { lista ->
                            val concatenated = lista.joinToString("\n") { it.nota }
                            //Html.fromHtml(concatenated, Html.FROM_HTML_MODE_COMPACT).trim()
                            //TODO: Meter el html .
                            concatenated
                        }.firstOrNull()
                        turno.asDomainModel2(estasTareas, coloresTrenes, notasTurno)
                    }
            }.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)


    private val trenesBuscados =
        combine(
            idGrafico,
            _tren,
            diaSemana,
            coloresTrenes
        ) { idGrafico, tren, diaSemana, coloresTrenes ->
            repository.getTurnoBuscadoPorTren(idGrafico, tren, diaSemana)
                .map { lista ->
                    lista.distinctBy {
                        it.turno to it.diaSemana to it.idGrafico
                    }.map { turno ->
                        val estasTareas = repository.getTareasDeUnTurnoBuscador2(
                            turno.idGrafico,
                            turno.turno,
                            turno.diaSemana.aSemanalSingle()
                        ).firstOrNull()
                        val notasTurno = repository.getNotasDelTurno(
                            turno.idGrafico,
                            turno.turno,
                            turno.diaSemana.aSemanalSingle()
                        ).map { lista ->
                            val concatenated = lista.joinToString("\n") { it.nota }
                            // Html.fromHtml(concatenated, Html.FROM_HTML_MODE_COMPACT).trim()
                            //TODO: Meter el html cuando podamos.
                            concatenated
                        }
                            .firstOrNull()
                        turno.asDomainModel2(estasTareas, coloresTrenes, notasTurno)
                    }
                }.firstOrNull()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), null)


    private val teleindicadoresBuscados = _trenDeTeleind.map { tren ->
        repository.getTeleindicadoresPorTren(tren).firstOrNull() ?: emptyList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())


    fun onTextChanged(text: String) {
        textfieldText.value = text
        when (buscando.value) {
            Buscando.TURNOS -> _turno.value = text.ifEmpty { "todo" }
            Buscando.TRENES -> _tren.value = text.ifEmpty { "nada" }
            Buscando.TELEIND -> _trenDeTeleind.value = text.ifEmpty { "todo" }
        }
    }

    fun onToggleClicked(bClicked: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (bClicked) {
                0 -> {
                    _turno.value = textfieldText.value.ifEmpty { "todo" }
                    _tren.value = "nada" //Borro los elementos de las otras listas.
                    _trenDeTeleind.value = ""
                    buscando.emit(Buscando.TURNOS)
                }

                1 -> {
                    _tren.value = textfieldText.value.ifEmpty { "nada" }
                    _turno.value = "" //Borro los elementos de las otras listas.
                    _trenDeTeleind.value = ""
                    buscando.emit(Buscando.TRENES)
                }

                2 -> {
                    _trenDeTeleind.value = textfieldText.value.ifEmpty { "todo" }
                    _turno.value = "" //Borro los elementos de las otras listas.
                    _tren.value = "nada"
                    buscando.emit(Buscando.TELEIND)
                }
            }
        }
    }

    val state = combine(
        buscando, textfieldText, hayTeleindicadores, listaGraficos, idGrafico,
        diaSemana, ordenBusqueda, dialogCompi, turnosBuscados, trenesBuscados,
        teleindicadoresBuscados, coloresTrenes, imagenEgaInitialScroll, imagenEgaTurno

    ) {array ->
        BuscadorState(
            array[0] as Buscando,
            array[1] as String,
            array[2] as Boolean,
            array[3] as List<GrGraficos>,
            array[4] as Long,
            array[5] as String,
            array[6] as OrdenBusqueda,
            array[7] as LsUsers,
            array[8] as List<TurnoBuscadorDM>?,
            array[9] as List<TurnoBuscadorDM>?,
            array[10] as List<OtTeleindicadores>,
            array[11] as List<OtColoresTrenes>?,
            array[12] as Int,
            array[13] as TurnoBuscadorDM?,
        )
    }


    //TODO: Esta función es llamada desde el init de este viewModel, cambiarlo.
    //De turnos compis = grafico, turno, diaSemana
    //De proxTareas = grafico, diaSemana, tren
    //De listaGraficos = gráfico.
    fun setInitialParameters(
        graficoPedido: Long?,
        diaPedido: Long?,//Sin uso aún.
        turnoPedido: String?,
        trenPedido: String?,
        diaSemanaPedido: String?
    ) {
        println("Vienes de $turnoPedido, $diaSemanaPedido, $graficoPedido")
        if (graficoPedido == null || graficoPedido == -1L) {
            viewModelScope.launch(Dispatchers.IO) {
                idGrafico.value =
                    repository.getIdGraficoDeUnDia(
                        Clock.System.todayIn(TimeZone.currentSystemDefault()).toEpochDays().toLong()
                    ).firstOrNull() ?: -1L
            }
            return
        }
        if (diaSemanaPedido == null) {// Viene de ListaGraficos
            idGrafico.value = graficoPedido
        } else {
            if (turnoPedido != null) {//Viene de turnos compis
                println("Vienes de turnos compis $turnoPedido, $diaSemanaPedido")
                idGrafico.value = graficoPedido
                textfieldText.value = turnoPedido
                _turno.value = turnoPedido
                buscando.value = Buscando.TURNOS
                diaSemana.value = diaSemanaPedido
            } else if (trenPedido != null) { //Viene de próximas tareas
                idGrafico.value = graficoPedido
                textfieldText.value = trenPedido
                _tren.value = trenPedido
                buscando.value = Buscando.TRENES
                diaSemana.value = diaSemanaPedido
            }
        }
    }

    fun onCompiClicked(idCompi: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val compi = coreRepo.getCompi(idCompi).firstOrNull() ?: LsUsers()
            dialogCompi.emit(compi)
        }
    }

    fun onCompiDismissed() {
        viewModelScope.launch(Dispatchers.IO) {
            dialogCompi.emit(LsUsers())
        }
    }

    fun onCallClick(phone: String) {
        val result = dialPhoneNumberUseCase(phone)

        viewModelScope.launch(Dispatchers.IO) {
            if (result is Result.Error && result.error == IntentNoAppError.NO_APPLICATION_AVAILABLE)
                _toastId.emit(Res.string.no_hay_una_aplicaci_n_para_hacer_llamadas)
        }
        onCompiDismissed()
    }

    fun onWhatsappClick(phone: String) {
        val result = abrirWhatsappUseCase(phone)
        viewModelScope.launch(Dispatchers.IO) {
            if (result is Result.Error && result.error == IntentNoAppError.NO_APPLICATION_AVAILABLE)
                _toastId.emit(Res.string.parece_que_no_tienes_instalado_whatsapp)
        }
        onCompiDismissed()
    }

    fun onImageEgaClicked(scrollValue: Int, offsetX: Float, turno: TurnoBuscadorDM) {
        val scrolled = scrollValue + offsetX
        val initialScroll = ((scrolled - 200).coerceAtLeast(0F) * 2.5).toInt()
        viewModelScope.launch {
            imagenEgaInitialScroll.value = initialScroll
            imagenEgaTurno.value = turno
        }
    }

    fun onDialogImagenEgaDimissed() {
        viewModelScope.launch {
            imagenEgaTurno.value = null
        }
    }

    fun onToastLaunched() {
        _toast.value = null
        _toastId.value = null
    }

    init {
        //TODO: Borrar esto y llamarlo desde la screen cuando recibamos las cosas de navigation.
        setInitialParameters(null, null,null,null,null)
    }



}

private fun String?.aSemanalSingle(): String {
    var posicion = 0
    this?.forEachIndexed { indice, char ->
        if (char == '1')
            posicion = indice
    }
    return when (posicion) {
        0 -> "L"
        1 -> "M"
        2 -> "X"
        3 -> "J"
        4 -> "V"
        5 -> "S"
        6 -> "D"
        else -> "F"
    }
}


fun TurnoBuscador.asDomainModel2(
    tareas: List<GrTareaBuscador>?,
    coloresTrenes: List<OtColoresTrenes>?,
    notasTurno: CharSequence?,
): TurnoBuscadorDM {
    return TurnoBuscadorDM(
        turno = turno,
        numeroTurno = numeroTurno,
        idGrafico = idGrafico,
        sitioOrigen = sitioOrigen,
        horaOrigen = horaOrigen,
        sitioFin = sitioFin,
        horaFin = horaFin,
        diaSemana = diaSemana,
        equivalencia = equivalencia,
        listaTareas = tareas.largas(),
        listaTareasCortas = tareas.cortas(),
        coloresTrenes = coloresTrenes,
        nombreCompi = nombreCompi,
        idCompi = idCompi,
        notas = notasTurno
    )
}

private fun List<GrTareaBuscador>?.largas(): List<GrTareaConClima>? {
    return this?.map { tarea ->
        GrTareaConClima(
            id = tarea.id,
            idGrafico = tarea.idGrafico,
            turno = tarea.turno,
            ordenServicio = tarea.ordenServicio,
            servicio = tarea.servicio,
            tipoServicio = tarea.tipoServicio,
            diaSemana = tarea.diaSemana,
            sitioOrigen = tarea.sitioOrigen,
            horaOrigen = tarea.horaOrigen,
            sitioFin = tarea.sitioFin,
            horaFin = tarea.horaFin,
            vehiculo = tarea.vehiculo,
            observaciones = tarea.observaciones,
            inserted = tarea.inserted,
            notasTren = tarea.notasTren,
        )
    }
}

private fun List<GrTareaBuscador>?.cortas(): List<GrTareas>? {
    return this?.map { tarea ->
        GrTareas(
            id = tarea.id,
            idGrafico = tarea.idGrafico,
            turno = tarea.turno,
            ordenServicio = tarea.ordenServicio,
            servicio = tarea.servicio,
            tipoServicio = tarea.tipoServicio,
            diaSemana = tarea.diaSemana,
            sitioOrigen = tarea.sOrigenCorto,
            horaOrigen = tarea.horaOrigen,
            sitioFin = tarea.sFinCorto,
            horaFin = tarea.horaFin,
            vehiculo = tarea.vehiculo,
            observaciones = tarea.observaciones,
            inserted = tarea.inserted,
        )
    }
}



