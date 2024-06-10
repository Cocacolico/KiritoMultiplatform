package es.kirito.kirito.menu.domain

import es.kirito.kirito.core.data.database.GrGraficos
import es.kirito.kirito.core.data.database.LsUsers
import es.kirito.kirito.menu.domain.models.DiasEspeciales

data class MenuState(
    val userID: Long = 0L,
    val allDataErased: Boolean = false,
    val userData: LsUsers? = null,
    val diasEspeciales: DiasEspeciales = DiasEspeciales(0,0,0,0,0,0,0),
    val graficoDeHoy: GrGraficos? = null,
    val graficoActualYprox: List<GrGraficos>? = null
)
