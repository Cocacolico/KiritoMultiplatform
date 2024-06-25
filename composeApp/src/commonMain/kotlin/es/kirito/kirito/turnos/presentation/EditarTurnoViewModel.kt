package es.kirito.kirito.turnos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.kirito.kirito.core.data.database.CuDetalle
import es.kirito.kirito.core.data.utils.KiritoException
import es.kirito.kirito.core.domain.CoreRepository
import es.kirito.kirito.core.domain.error.errorKiritoCodeText
import es.kirito.kirito.turnos.domain.EditarTurnoState
import es.kirito.kirito.turnos.domain.TurnosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import org.jetbrains.compose.resources.StringResource

@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalCoroutinesApi::class)
class EditarTurnoViewModel : ViewModel(), KoinComponent {

    private val repository: TurnosRepository by inject()
    private val coreRepo: CoreRepository by inject()

    val toastString = MutableSharedFlow<String?>()
    val toastId = MutableSharedFlow<StringResource?>()

    private val selectedDate =
        MutableStateFlow(Clock.System.todayIn(TimeZone.currentSystemDefault()))

    private val showDiasDebe = MutableStateFlow(false)

    private val selectedShift = selectedDate.flatMapLatest {
        val turno = repository.getCuDetalleDeUnDia(it.toEpochDays().toLong())
        turno.mapLatest { cuDetalle ->
            showDiasDebe.value = cuDetalle != null && (cuDetalle.comj != 0 || cuDetalle.libra != 0)
        }
        turno
    }.distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    private val editedShift =
        MutableStateFlow(
            CuDetalle(
                idDetalle = -1L,
                idUsuario = -1L,
                fecha = -1L,
                diaSemana = null,
                turno = null,
                tipo = "",
                notas = "",
                nombreDebe = "",
                updated = null,
                libra = null,
                comj = null,
                excesos = null,
                mermas = null,
                excesosGrafico = null
            )
        )


    private val doneEditting = MutableStateFlow(false)
    private val listaUsuariosEnNombreDebe =
        coreRepo.usuariosEnNombreDebe.combine(editedShift) { nombres: List<String>, filtro: CuDetalle ->
            nombres.filter { it.contains(filtro.nombreDebe) }
        }

    val state = combine(
        selectedDate, showDiasDebe, selectedShift, editedShift, listaUsuariosEnNombreDebe,
        doneEditting
    ) { array ->
        EditarTurnoState(
            selectedDate = array[0] as LocalDate,
            showDiasDebe = array[1] as Boolean,
            selectedShift = array[2] as CuDetalle? ?: CuDetalle(),
            editedShift = array[3] as CuDetalle,
            usuariosEnNombreDebe = array[4] as List<String>,
            doneEditting = array[5] as Boolean,
        )
    }


    fun onComjSelected(cantidad: Int) {
        editedShift.update {
            it.copy(comj = cantidad)
        }
    }

    fun onLibraSelected(cantidad: Int) {
        editedShift.update {
            it.copy(libra = cantidad)
        }
    }

    fun onTipoSelected(tipo: String) {
        editedShift.update {
            it.copy(tipo = tipo)
        }
    }

    fun onNotasChanged(nota: String) {
        editedShift.update {
            it.copy(notas = nota)
        }
    }

    fun onTurnoTextChanged(text: String) {
        editedShift.update {
            it.copy(turno = text)
        }
    }

    fun onCompiTextChanged(text: String) {
        editedShift.update {
            it.copy(nombreDebe = text)
        }
    }

    fun onShowDiasDebeClick() {
        showDiasDebe.value = true
    }

    fun onNextDayClick() {
        //TODO: Guardar el día actual.
        selectedDate.update { fecha -> fecha.plus(1, DateTimeUnit.DAY) }
    }

    fun onGuardarClick() {
        modificarClave(false)
    }

    fun onGuardarYSiguienteClick() {
        modificarClave(true)
    }

    fun isTheShiftModified(): Boolean {
        val turnoEditado = editedShift.value
        val turnoOriginal = selectedShift.value

        var modified = false
        if (turnoOriginal?.turno != turnoEditado.turno ||
            turnoOriginal?.tipo != turnoEditado.tipo ||
            turnoOriginal.comj != turnoEditado.comj ||
            turnoOriginal.libra != turnoEditado.libra ||
            //    Html.fromHtml(turnoOriginal.notas, Html.FROM_HTML_MODE_COMPACT).toString().trim()
            //    != Html.fromHtml(turnoEditado.notas, Html.FROM_HTML_MODE_COMPACT).toString().trim() ||
            //TODO: HTML parser!!

            turnoOriginal.nombreDebe != turnoEditado.nombreDebe
        )
            modified = true
        return modified
    }

    private fun modificarClave(goToNextDay: Boolean) {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.editSingleShift(selectedShift.value, editedShift.value) {
                    if (goToNextDay)
                        selectedDate.value = selectedDate.value.plus(1, DateTimeUnit.DAY)
                    else
                        doneEditting.value = true
                }
            } catch (e: KiritoException) {
                toastId.emit(errorKiritoCodeText(e.message?.toIntOrNull() ?: 1))
            } catch (e: Exception) {
                toastString.emit(e.message)
            }


        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            initializeEditedShift()
        }
    }

    private suspend fun initializeEditedShift() {
        combine(selectedDate, selectedShift) { date, shift ->
            if (shift?.fecha == date.toEpochDays().toLong())
                shift
            else
                CuDetalle()
        }.distinctUntilChanged()
            .collectLatest { turno ->
                println("Reajustando desde initialize")
                editedShift.emit(turno)
            }
    }

    fun onToastLaunched() {
        //Limpiamos los toasts para que no se vuelvan a emitir.
        viewModelScope.launch {
            toastString.emit(null)
            toastId.emit(null)
        }
    }


}