package es.kirito.kirito.turnos.domain

import es.kirito.kirito.core.data.database.CuHistorial
import es.kirito.kirito.core.data.database.GrTareas
import es.kirito.kirito.core.data.database.Localizador
import es.kirito.kirito.core.data.database.OtColoresTrenes
import es.kirito.kirito.core.domain.models.CuDetalleConFestivoDBModel
import es.kirito.kirito.core.domain.models.GrTarea
import es.kirito.kirito.core.domain.models.TurnoPrxTr
import es.kirito.kirito.turnos.domain.models.ErroresHoy
import es.kirito.kirito.turnos.domain.models.TeleindicadoresDeTren
import es.kirito.kirito.turnos.presentation.HoyViewModel
import kotlinx.datetime.LocalDate

data class HoyState(
    val date: LocalDate? = null,
    val cuDetalle: CuDetalleConFestivoDBModel? = null,
    val turnoPrxTr: TurnoPrxTr? = null,
    val coloresTrenes: List<OtColoresTrenes> = emptyList(),
    val festivo: String = "",
    val tareasCortas: List<GrTareas> = emptyList(),
    val tareas: List<GrTarea> = emptyList(),
    val teleindicadores: List<TeleindicadoresDeTren> = emptyList(),
    val notasUsuario: String = "",
    val notasTurno: String = "",
    val advMermasTurnosLaterales: HoyViewModel.AdvmermasTurnosLaterales = HoyViewModel.AdvmermasTurnosLaterales(false),
    val historial: List<CuHistorial> = emptyList(),
    val iHaveShiftsShared: Boolean = false,
    val erroresHoy: ErroresHoy = ErroresHoy(),
    val localizador: Localizador? = null,
    val showLocalizadorDialog: Boolean = false,



    )

