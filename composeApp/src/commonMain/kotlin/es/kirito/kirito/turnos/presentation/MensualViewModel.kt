@file:Suppress("UNCHECKED_CAST")

package es.kirito.kirito.turnos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.kirito.kirito.core.data.dataStore.preferenciasKirito
import es.kirito.kirito.core.domain.CoreRepository
import es.kirito.kirito.core.domain.models.CuDetalleConFestivoDBModel
import es.kirito.kirito.core.domain.models.TurnoPrxTr
import es.kirito.kirito.core.domain.util.lastDayOfMonth
import es.kirito.kirito.core.domain.util.toLocalDate
import es.kirito.kirito.core.domain.util.withDayOfMonth
import es.kirito.kirito.turnos.domain.MensualState
import es.kirito.kirito.turnos.domain.TurnosRepository
import es.kirito.kirito.turnos.domain.models.CuDetalleConFestivoSemanal
import es.kirito.kirito.turnos.domain.models.CuadroAnualVacio
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.cuadro_subido_correctamente
import kirito.composeapp.generated.resources.descargando_grafico
import kirito.composeapp.generated.resources.no_hay_grafico_en_vigor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.StringResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(ExperimentalCoroutinesApi::class)
class MensualViewModel : ViewModel(), KoinComponent {
    private val repository: TurnosRepository by inject()
    private val coreRepo: CoreRepository by inject()


    private val cambiosActivados = preferenciasKirito.map { it.userId }.flatMapLatest { id ->
        repository.tengoLosCambiosActivados(id)
    }

    private val selectedMonth =
        MutableStateFlow(Clock.System.todayIn(TimeZone.currentSystemDefault()))

    private val selectedDate = MutableStateFlow<LocalDate?>(null)


    private val selectedCuDetalle = selectedDate.flatMapLatest { fecha ->
        repository.getCuDetallesDeUnDia(fecha?.toEpochDays())
    }
    private val selectedPrxTr = selectedDate.flatMapLatest {
        repository.getTurnoDeUnDia(it?.toEpochDays())
    }

    private val esteDiaTieneGrafico = selectedDate.flatMapLatest {
        repository.fechaTieneExcelIF(it?.toEpochDays()?.toLong())
    }


    private val selectedDates = combine(selectedDate, selectedMonth) { date, month ->
        Pair(date, month)
    }

    private val turnosDelSemanal = selectedDates.flatMapLatest { dates ->
        val date = dates.first
        val month = dates.second
        val flowHoraInicio = repository.getTurnosEntreFechas(
            month.withDayOfMonth(1).toEpochDays().toLong(),
            month.lastDayOfMonth().toEpochDays().toLong()
        )
        repository.getCuDetallesConFestivos(
            month.withDayOfMonth(1).toEpochDays().toLong(),
            month.lastDayOfMonth().toEpochDays().toLong()
        ).map { lista ->
            lista.map { turno ->
                turno.copy(isSelected = turno.fecha == date)
            }
        }
            .zip(flowHoraInicio) { cuDetallesConFestivosModels: List<CuDetalleConFestivoSemanal>, turnosPrxTrs: List<TurnoPrxTr> ->
                cuDetallesConFestivosModels.map { semanalObj ->
                    val matchingPruebaObj =
                        turnosPrxTrs.find { it.fecha.toLocalDate() == semanalObj.fecha }
                    if (matchingPruebaObj != null) {
                        if (matchingPruebaObj.indicador != 0)
                            semanalObj.copy(
                                horaInicio = matchingPruebaObj.horaOrigen,
                                horaFin = matchingPruebaObj.horaFin,
                                color = matchingPruebaObj.color
                            )
                        else
                            semanalObj.copy(
                                horaInicio = matchingPruebaObj.horaOrigen,
                                horaFin = matchingPruebaObj.horaFin
                            )
                    } else {
                        semanalObj
                    }
                }
            }
    }


    private val festivo = selectedDate.flatMapLatest {
        repository.getFestivoDeUnDia(it?.toEpochDays())
    }
    private val iHaveShiftsShared = preferenciasKirito.map { it.userId }.flatMapLatest { id ->
        repository.tengoLosCambiosActivados(id)
    }
    val toastString = MutableSharedFlow<String?>()
    val toastId = MutableSharedFlow<StringResource?>()

    val mensualState = combine(
        cambiosActivados, selectedMonth, selectedDate, selectedCuDetalle,
        selectedPrxTr, esteDiaTieneGrafico, turnosDelSemanal, festivo,
        iHaveShiftsShared
    ) { array ->
        MensualState(
            array[0] as Boolean,
            array[1] as LocalDate,
            array[2] as LocalDate?,
            array[3] as CuDetalleConFestivoDBModel?,
            array[4] as TurnoPrxTr?,
            array[5] as Boolean,
            array[6] as List<CuDetalleConFestivoSemanal>,
            array[7] as String?,
            array[8] as Boolean,
        )
    }


    //TODO: Flag logout.
//    val flagLogout = repository.checkLogoutFlag()
//    fun logout(context: Context) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.nukeAll(context)
//        }
//    }

    fun generarCuadroVacio(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.requestSubirCuadroVacio(
                    CuadroAnualVacio(
                        year = selectedMonth.value.year,
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

    fun clearToasts() {
        viewModelScope.launch {
            toastId.emit(null)
            toastString.emit(null)
        }
    }


    fun onBulkEditClick() {
        //TODO: Navegar
    }

    fun onExcessClick() {
        //TODO: Navegar
    }

    fun onEditClick() {
        //TODO: Navegar
    }

    fun onExchangeClick() {
        //TODO: Navegar
    }

    fun setSelectedMonth(fecha: LocalDate) {
        selectedMonth.value = fecha
        clearSelectedDate()
    }

    //Cargan nuevo contenido en la tabla, si hace falta.
    fun nextMonth(meses: Long = 1) {
        selectedMonth.value = selectedMonth.value.plus(meses, DateTimeUnit.MONTH)
        clearSelectedDate()
    }

    fun previousMonth(meses: Long = 1) {
        selectedMonth.value = selectedMonth.value.minus(meses, DateTimeUnit.MONTH)
        clearSelectedDate()
    }


    fun clearSelectedDate() {
        selectedDate.value = null
    }

    fun onDateSelected(turno: CuDetalleConFestivoSemanal) {
        selectedDate.value = turno.fecha
    }

    private fun isGraficoDownloaded() {
        viewModelScope.launch(Dispatchers.IO) {
            mensualState
                .map { state ->
                    if (state.selectedDate != null && !state.esteDiaTieneGrafico)
                        state.selectedDate
                    else null
                }
                .distinctUntilChanged()
                .collectLatest { fechaQueDescargar ->
                    delay(50)//Junto con collectLatest, me garantiza que solo se ejecuta
                    // cuando de verdad debe.
                    if (fechaQueDescargar != null)
                        descargarUnGrafico(fechaQueDescargar.toEpochDays())
                }
        }
    }


    //Nos bajamos un gráfico si resulta que donde pinchamos no hay uno.
    private suspend fun descargarUnGrafico(fecha: Int) {
        val idGrafico = repository.getIdGraficoDeUnDia(fecha.toLong()).firstOrNull()
        println("Voy a bajarme el gráfico " + idGrafico.toString())
        if (idGrafico != null// && hayInternet() //TODO: Comprobar si hay internet.
        ) {
            try {
                toastId.emit(Res.string.descargando_grafico)
                println("Descargando gráfico $idGrafico")
                coreRepo.descargarComplementosDelGrafico(listOf(idGrafico.toString()))

            } catch (e: Exception) {
                if (e.message != "Ignorar") {
                    //TODO: Firebase
                    // FirebaseCrashlytics.getInstance().recordException(e)
                    toastString.emit(e.message)
                }
                println("Error: ${e.message} tras descargar un grafico")
            }

        } else if (idGrafico == null) {//Realmente es que este día no hay un gráfico asignado.
            toastId.emit(Res.string.no_hay_grafico_en_vigor)
        }
    }


    init {
        selectedMonth.value = Clock.System.todayIn(TimeZone.currentSystemDefault())
        isGraficoDownloaded()
    }


}