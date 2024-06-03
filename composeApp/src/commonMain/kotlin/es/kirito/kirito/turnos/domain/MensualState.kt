package es.kirito.kirito.turnos.domain

import es.kirito.kirito.core.data.database.CuDetalle
import es.kirito.kirito.core.domain.models.CuDetalleConFestivoDBModel
import es.kirito.kirito.core.domain.models.TurnoPrxTr
import es.kirito.kirito.turnos.domain.models.CuDetalleConFestivoSemanal
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

data class MensualState(
    val cambiosActivados: Boolean = false,
    val selectedMonth: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val selectedDate: LocalDate? = null,
    val selectedCuDetalle: CuDetalleConFestivoDBModel? = null,
    val selectedPrxTr: TurnoPrxTr? = null,
    val esteDiaTieneGrafico: Boolean = false,
    val turnosDelSemanal: List<CuDetalleConFestivoSemanal> = emptyList(),
    val festivo: String? = null
)