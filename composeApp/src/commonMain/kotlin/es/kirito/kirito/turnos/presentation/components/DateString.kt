package es.kirito.kirito.turnos.presentation.components

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewModelScope
import es.kirito.kirito.core.domain.util.enCastellano
import es.kirito.kirito.core.domain.util.enMiFormato
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.ayer__fechaxx
import kirito.composeapp.generated.resources.hoy__fechaxx
import kirito.composeapp.generated.resources.manana__fechaxx
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.stringResource

@Composable
fun DateString(date: LocalDate?): String {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val prefixText = when (date) {
        today -> stringResource(Res.string.hoy__fechaxx)
        today.plus(1,DateTimeUnit.DAY)
           -> stringResource(Res.string.manana__fechaxx)

        today.minus(1,DateTimeUnit.DAY)
        -> stringResource(Res.string.ayer__fechaxx)

        else -> ""
    }
    val text = prefixText + date?.dayOfWeek?.enCastellano() +
            ", " + date?.enMiFormato()
    return text

}


