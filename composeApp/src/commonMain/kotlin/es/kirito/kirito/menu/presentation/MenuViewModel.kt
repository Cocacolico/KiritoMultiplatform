package es.kirito.kirito.menu.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.kirito.kirito.core.data.dataStore.preferenciasKirito
import es.kirito.kirito.core.data.database.GrGraficos
import es.kirito.kirito.core.data.database.LsUsers
import es.kirito.kirito.core.data.utils.openStore
import es.kirito.kirito.core.domain.CoreRepository
import es.kirito.kirito.menu.domain.MenuRepository
import es.kirito.kirito.menu.domain.MenuState
import es.kirito.kirito.menu.domain.models.DiasEspeciales
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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
    private val allDataErased  = MutableStateFlow(false)

    private val flagLogout = repository.checkLogoutFlag()

    private val showLogoutDialog = MutableStateFlow(false)
    private val showUpdateDialog = MutableStateFlow(false)
    private val showMandatoryUpdateDialog = MutableStateFlow(false)

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
    private val diasInicialesChecked = date.flatMapLatest { date ->
        repository.areDiasInicialesInitialised(date!!.year)
    }

    val anio = date.value!!.year
    private val inicioAnio = LocalDate(year = anio,1,1).toEpochDays().toLong()//LocalDate.now().withDayOfYear(1).toEpochDay()
    private val finAnio = LocalDate(year = anio,12,31).toEpochDays().toLong()//LocalDate.now().withMonth(12).withDayOfMonth(31).toEpochDay()
    private val lzs = repository.getNumLzs(inicioAnio, finAnio, anio)
    private val lzas = repository.getNumLzas(inicioAnio, finAnio, anio)
    private val comjs = repository.getNumComjs(inicioAnio, finAnio, anio)
    private val libras = repository.getNumLibras(inicioAnio, finAnio, anio)
    private val dds = repository.getNumDds(inicioAnio, finAnio, anio)
    private val djs = repository.getNumDjs(inicioAnio, finAnio, anio)
    private val djas = repository.getNumDjas(inicioAnio, finAnio, anio)
    private val diasEspeciales = combine(lzs, lzas, comjs, libras, dds, djs, djas){array ->
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

    val menuState = combine(
        userID, allDataErased, miUsuario, diasEspeciales, graficoDeHoy,graficoActualYprox,
        cambiosNuevos,mensajesAdminNuevos, diasInicialesChecked, flagLogout,
        showLogoutDialog, showUpdateDialog, showMandatoryUpdateDialog
    ) {
        array ->
        MenuState(
            array[0] as Long,
            array[1] as Boolean,
            array[2] as LsUsers,
            array[3] as DiasEspeciales,
            array[4] as GrGraficos,
            array[5] as List<GrGraficos>,
            array[6] as Int,
            array[7] as Int,
            array[8] as Int,
            array[9] as Int,
            array[10] as Boolean,
            array[11] as Boolean,
            array[12] as Boolean
        )
    }
    fun onPerfilClick() {
        TODO("Not yet implemented")
    }
    fun onLogoutClick() {
        showLogoutDialog.value = true
    }

    fun onMustUpdDismiss() {
        logout()
    }
    fun onConfirmUpd() {
        openStore()
    }
    fun showUpdateDialog(mandatory: Boolean) {
        if(mandatory)
            showMandatoryUpdateDialog.value = true
        else
            showUpdateDialog.value = true
    }
    fun onUpdDismiss() {
        showUpdateDialog.value = false
    }
    fun onWrongTokenDismiss() {
        logout()
    }
    fun onConfirmWrongToken() {
        logout()
    }
    fun onLogoutDialogDismiss() {
        showLogoutDialog.value = false
    }
    fun onLogoutDialogConfirm() {
        logout()
    }
    private fun logout() { // TODO Aún no funciona correctamente la función nukeAll
        viewModelScope.launch(Dispatchers.IO) {
            coreRepo.nukeAll()
            allDataErased.value = true
        }
    }
}