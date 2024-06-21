package es.kirito.kirito.turnos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.kirito.kirito.core.data.database.CuDetalle
import es.kirito.kirito.core.domain.CoreRepository
import es.kirito.kirito.turnos.domain.EditarTurnoState
import es.kirito.kirito.turnos.domain.TurnosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
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
import es.kirito.kirito.core.domain.util.esTurnoConNumero
import es.kirito.kirito.core.domain.util.esTurnoConNumeroONumero
import es.kirito.kirito.core.domain.util.esTipoValido
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus

@Suppress("UNCHECKED_CAST")
@OptIn(ExperimentalCoroutinesApi::class)
class EditarTurnoViewModel : ViewModel(), KoinComponent {

    private val repository: TurnosRepository by inject()
    private val coreRepo: CoreRepository by inject()

    private val selectedDate =
        MutableStateFlow(Clock.System.todayIn(TimeZone.currentSystemDefault()))

    private val goToNextDay = false
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
    private val textTurno = MutableStateFlow("")
    private val textNombreDebe = MutableStateFlow("")
    private val listaUsuariosEnNombreDebe =
        coreRepo.usuariosEnNombreDebe.combine(textNombreDebe) { nombres: List<String>, filtro: String ->
            nombres.filter { it.contains(filtro) }
        }

    val state = combine(
        selectedDate, showDiasDebe, selectedShift, editedShift, listaUsuariosEnNombreDebe,
        doneEditting, textTurno, textNombreDebe
    ) { array ->
        EditarTurnoState(
            selectedDate = array[0] as LocalDate,
            showDiasDebe = array[1] as Boolean,
            selectedShift = array[2] as CuDetalle? ?: CuDetalle(),
            editedShift = array[3] as CuDetalle,
            usuariosEnNombreDebe = array[4] as List<String>,
            doneEditting = array[5] as Boolean,
            textTurno = array[6] as String,
            textNombreDebe = array[7] as String,
        )
    }


    fun setSelectedDate(entrada: LocalDate) {
        selectedDate.value = entrada
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

    fun onTextChanged(text: String) {
        textTurno.update { text }
    }

    fun onCompiTextChanged(text: String) {
        textNombreDebe.update { text }
    }

    fun onShowDiasDebeClick(){
        showDiasDebe.value = true
    }
    fun onNextDayClick() {
        //TODO: Guardar el día actual.
        selectedDate.update { fecha -> fecha.plus(1, DateTimeUnit.DAY) }
    }

    fun onGuardarClick(){
        modificarClave()
    }

    fun onGuardarYSiguienteClick(){
        modificarClave()
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

    private fun modificarClave() {

        viewModelScope.launch(Dispatchers.IO) {
            val turnoEditado = editedShift.value
            val turnoOriginal = selectedShift.value


            //En primer lugar miramos si hemos modificado el turno con una equivalencia:
            if (turnoOriginal?.turno != turnoEditado.turno) {
                val idGrafico = repository.getIdGraficoDeUnDia(turnoEditado.fecha).first()
                val turnoDesdeEquivalencia =
                    repository.getTurnoDeEquivalencia(
                        turnoEditado.turno ?: "",
                        idGrafico,
                        turnoEditado.diaSemana
                    ).first()
                println(turnoEditado.toString() + " " + turnoDesdeEquivalencia.toString())
                if (turnoDesdeEquivalencia?.turno != null)
                    turnoEditado.turno = turnoDesdeEquivalencia.turno
            }
            //Después, preparamos el tipo y turno para hacerlos equivalentes:
            if (turnoOriginal?.turno == turnoEditado.turno //No hemos modificado el turno
                && turnoOriginal?.tipo != turnoEditado.tipo // pero sí el tipo.
            ) {
                if (!turnoEditado.tipo.esTurnoConNumero()) {
                    //Este tipo no es para turnos con número, de serlo, así lo mandamos.
                    //Cambiamos el turno a lo mismo que el tipo, para evitar el rebote doble del workManager:
                    turnoEditado.turno = turnoEditado.tipo
                }
            } else if (turnoOriginal?.turno != turnoEditado.turno //Hemos modificado el turno
                && turnoOriginal?.tipo == turnoEditado.tipo // y no el tipo.
            ) {
                if (!turnoEditado.turno.esTurnoConNumeroONumero()) {
                    //No es un turno con número ni un número, modificamos entonces el tipo también.
                    if (turnoEditado.turno.esTipoValido() &&
                        turnoEditado.tipo != "DD"
                    )
                        turnoEditado.tipo = turnoEditado.turno ?: ""
                }
                if (turnoEditado.turno?.toIntOrNull() != null &&
                    !turnoEditado.tipo.esTurnoConNumero()
                ) {
                    //El turno es un número y el tipo NO es de los que acepta números. Lo cambio a T.
                    turnoEditado.tipo = "T"//Lo cambio a T.
                }
            }
//
//            val worker = OneTimeWorkRequest.Builder(ShiftEditWorker::class.java)
//            val data = Data.Builder()
//            with(turnoEditado) {
//                data.putLong("id_detalle", id_detalle)
//                data.putLong("fecha", fecha)
//                data.putString("turno", turno)
//                data.putString("tipo", tipo)
//                data.putString("notas", notas)
//                data.putString("nombre_debe", nombre_debe)
//                data.putInt("LIBRa", libra ?: 0)
//                data.putInt("COMJ", comj ?: 0)
//                data.putString("lOrigen", tEsp.lOrigen)
//                data.putInt("hOrigen", tEsp.hOrigen)
//                data.putString("lFin", tEsp.lFin)
//                data.putInt("hFin", tEsp.hFin)
//
//            }
//            val constraints = Constraints.Builder()
//                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .build()
//
//            worker
//                .addTag(WORK_SHIFT_EDIT)
//                .setInputData(data.build())
//                .setConstraints(constraints)
//                .build()
//            workManager.enqueue(worker.build())

            println("Pues guardaríamos el turno $turnoEditado")
//            if (goToNextDay) {
//                selectedDate.emit(selectedDate.value?.plus(1))
//                goToNextDay = false
//            }
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


}