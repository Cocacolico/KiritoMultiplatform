@file:OptIn(KoinExperimentalAPI::class)

package es.kirito.kirito.turnos.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import es.kirito.kirito.core.domain.util.enCastellano
import es.kirito.kirito.core.presentation.components.HeaderWithPrevNext
import es.kirito.kirito.turnos.domain.MensualState
import es.kirito.kirito.turnos.presentation.components.MensualGridItem
import es.kirito.kirito.turnos.presentation.components.MensualSelectedDate
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.month_and_year
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun MensualScreen(navController: NavHostController) {
    val viewModel = koinViewModel<MensualViewModel>()
    val mensualState by viewModel.mensualState.collectAsState(initial = MensualState())



    Surface(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxWidth()){
            HeaderWithPrevNext(
                title = stringResource(Res.string.month_and_year,
                    mensualState.selectedMonth.month.enCastellano(),
                    mensualState.selectedMonth.year),
                festivo = "",
                onDateClick = {
                              //TODO: Hacer el diálogo!
                           //   viewModel.setSelectedMonth()
                },
                onFestivoClick = {},//No lo usamos.
                onNextClick = { viewModel.nextMonth(1) },
                onNextLongClick = {viewModel.nextMonth(12)},
                onPrevClick = {viewModel.previousMonth(1)},
                onPrevLongClick = {viewModel.previousMonth(12)}
            )
            MensualBody(viewModel)
        }


    }
}

@Composable
fun MensualBody(viewModel: MensualViewModel){

    val mensualState by viewModel.mensualState.collectAsState(initial = MensualState())
    val primerDia = (mensualState.turnosDelSemanal.getOrNull(0)?.fecha?.dayOfWeek?.ordinal) ?: 0


    LazyVerticalGrid(GridCells.Fixed(7), modifier = Modifier){

        items(primerDia){
            Box {  }
        }

        items(mensualState.turnosDelSemanal){turno ->
            MensualGridItem(turno){
                viewModel.onDateSelected(turno)
            }
        }
        if (mensualState.selectedDate != null)
            item(span = { GridItemSpan(7) }) {
                MensualSelectedDate(
                    mensualState,
                    onFestivoClick = {},
                    onMasDetallesClick = {},
                    onComjYLibraClick = {},
                    onExcesosClick = {},
                )
            }

    }
}