package es.kirito.kirito.menu.domain

import es.kirito.kirito.core.data.database.GrGraficos
import es.kirito.kirito.core.data.database.LsUsers
import es.kirito.kirito.menu.domain.models.DiasEspeciales

data class MenuState(
    val userID: Long = 0L,
    val allDataErased: Boolean = false,
    val userData: LsUsers = LsUsers(),
    val diasEspeciales: DiasEspeciales = DiasEspeciales(0,0,0,0,0,0,0),
    val graficoDeHoy: GrGraficos? = null,
    val graficoActualYprox: List<GrGraficos> = emptyList(),
    val cambiosNuevos: Int = 0,
    val mensajesAdminNuevos: Int = 0,
    val diasInicialesChecked: Int = 0,
    val flagLogout: Int = 0,
    val showLogoutDialog: Boolean = false,
    val showUpdateDialog: Boolean = false
)
