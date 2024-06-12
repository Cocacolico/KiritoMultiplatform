package es.kirito.kirito.turnos.domain

import es.kirito.kirito.core.data.database.GrGraficos
import es.kirito.kirito.core.data.database.LsUsers
import es.kirito.kirito.core.data.database.OtColoresTrenes
import es.kirito.kirito.core.data.database.OtTeleindicadores
import es.kirito.kirito.turnos.domain.models.Buscando
import es.kirito.kirito.turnos.domain.models.OrdenBusqueda
import es.kirito.kirito.turnos.domain.models.TurnoBuscadorDM

data class BuscadorState(

    val buscando: Buscando = Buscando.TURNOS,
    val textfieldText: String = "",
    val hayTeleindicadores: Boolean = false,
    val listaGraficos: List<GrGraficos> = emptyList(),
    val selectedIdGrafico: Long = -1L,
    val selectedDiaSemana: String = "todo",
    val ordenBusqueda: OrdenBusqueda = OrdenBusqueda.CLAVE,
    val compi: LsUsers = LsUsers(),
    val turnosBuscados: List<TurnoBuscadorDM>? = null,
    val trenesBuscados: List<TurnoBuscadorDM>? = null,
    val teleindicadoresBuscados: List<OtTeleindicadores> = emptyList(),
    val coloresTrenes: List<OtColoresTrenes>? = null,
    val imagenEgaInitialScroll: Int = 0,
    val imagenEgaTurno: TurnoBuscadorDM? = null,
)