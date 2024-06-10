package es.kirito.kirito.menu.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.kirito.kirito.core.data.dataStore.preferenciasKirito
import es.kirito.kirito.core.data.database.GrGraficos
import es.kirito.kirito.core.data.database.LsUsers
import es.kirito.kirito.core.domain.CoreRepository
import es.kirito.kirito.menu.domain.MenuRepository
import es.kirito.kirito.menu.domain.MenuState
import es.kirito.kirito.menu.domain.models.DiasEspeciales
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(ExperimentalCoroutinesApi::class)
class MenuViewModel: ViewModel(), KoinComponent {

    private val coreRepo: CoreRepository by inject()
    private val repository: MenuRepository by inject()
    private val preferences = preferenciasKirito

    private val userID = preferences.map { it.userId }

    private val miUsuario = userID.flatMapLatest { id ->
        coreRepo.getMyUser(id)
    }

    private val _allDataErased = MutableStateFlow(false)
    private val allDataErased: Flow<Boolean> = _allDataErased

    val flagLogout = repository.checkLogoutFlag()

    private val date: MutableStateFlow<LocalDate?> =
        MutableStateFlow(Clock.System.todayIn(TimeZone.currentSystemDefault()))

    private val graficoActualYprox = date.flatMapLatest { date ->
        repository.getGraficosDeEnCatorceDias(date?.toEpochDays()?.toLong())
    }
    private val graficoDeHoy = date.flatMapLatest { date ->
        repository.getGraficoDeUnDia(date?.toEpochDays()?.toLong())
    }
    //Badges:
    val cambiosNuevos = repository.cambiosNuevos
    val mensajesAdminNuevos = repository.mensajesAdminNuevos
    val diasInicialesChecked = date.flatMapLatest { date ->
        repository.areDiasInicialesInitialised(date!!.year)
    }
    val anio = date.value!!.year
    val inicioAnio = LocalDate(year = anio,1,1).toEpochDays().toLong()//LocalDate.now().withDayOfYear(1).toEpochDay()
    val finAnio = LocalDate(year = anio,12,31).toEpochDays().toLong()//LocalDate.now().withMonth(12).withDayOfMonth(31).toEpochDay()
    val lzs = repository.getNumLzs(inicioAnio, finAnio, anio)
    val lzas = repository.getNumLzas(inicioAnio, finAnio, anio)
    val comjs = repository.getNumComjs(inicioAnio, finAnio, anio)
    val libras = repository.getNumLibras(inicioAnio, finAnio, anio)
    val dds = repository.getNumDds(inicioAnio, finAnio, anio)
    val djs = repository.getNumDjs(inicioAnio, finAnio, anio)
    val djas = repository.getNumDjas(inicioAnio, finAnio, anio)
    val diasEspeciales = combine(lzs, lzas, comjs, libras, dds, djs, djas){array ->
        var diasEspeciales = DiasEspeciales()
        array.forEachIndexed { index, i ->
            when(index){
                0 -> diasEspeciales = diasEspeciales.copy(lz = i ?: 0)
                1 -> diasEspeciales = diasEspeciales.copy(lza = i ?: 0)
                2 -> diasEspeciales = diasEspeciales.copy(comj = i ?: 0)
                3 -> diasEspeciales = diasEspeciales.copy(libra = i ?: 0)
                4 -> diasEspeciales = diasEspeciales.copy(dd = i ?: 0)
                5 -> diasEspeciales = diasEspeciales.copy(dj = i ?: 0)
                6 -> diasEspeciales = diasEspeciales.copy(dja = i ?: 0)
            }
        }
        diasEspeciales
    }

    /*private suspend fun getUserID() {
        _state.update {
            it.copy(userID = preferences.first().userId)
        }
    }
    private fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            coreRepo.nukeAll()
            _state.update {
                it.copy(allDataErased = true)
            }
        }
    }*/
    val menuState = combine(
        userID, allDataErased, miUsuario, diasEspeciales, graficoDeHoy,graficoActualYprox
    ) {
        array ->
        MenuState(
            array[0] as Long,
            array[1] as Boolean,
            array[2] as LsUsers,
            array[3] as DiasEspeciales,
            array[4] as GrGraficos,
            array[5] as List<GrGraficos>
        )
    }
    fun onPerfilClick() {
        TODO("Not yet implemented")
    }

    fun onLogoutClick() {
        //logout()
    }
    /*init {
        viewModelScope.launch(Dispatchers.IO) {
            getUserID()
        }
    }*/
}