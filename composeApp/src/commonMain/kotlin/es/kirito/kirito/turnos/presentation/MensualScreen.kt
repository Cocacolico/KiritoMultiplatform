@file:OptIn(KoinExperimentalAPI::class)

package es.kirito.kirito.turnos.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import es.kirito.kirito.core.domain.util.enCastellano
import es.kirito.kirito.core.presentation.components.HeaderWithPrevNext
import es.kirito.kirito.core.presentation.components.LongToast
import es.kirito.kirito.core.presentation.components.MyDialogInformation
import es.kirito.kirito.turnos.domain.MensualState
import es.kirito.kirito.turnos.presentation.components.DialogEditShift
import es.kirito.kirito.turnos.presentation.components.MensualGridItem
import es.kirito.kirito.turnos.presentation.components.MensualSelectedDate
import es.kirito.kirito.turnos.presentation.utils.TextoExplicComjsYLibras
import es.kirito.kirito.turnos.presentation.utils.TextoExplicMermaGenerada
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources._0
import kirito.composeapp.generated.resources.month_and_year
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun MensualScreen(navController: NavHostController) {
    val viewModel = koinViewModel<MensualViewModel>()
    val mensualState by viewModel.mensualState.collectAsState(initial = MensualState())
    var festivoToast by remember { mutableStateOf("") }
    var comjLibraToast by remember { mutableStateOf(false) }
    var showExcesosDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    val toastString by viewModel.toastString.collectAsState(null)
    val toastId by viewModel.toastId.collectAsState(null)



    Surface(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxWidth()) {
                HeaderWithPrevNext(
                    title = stringResource(
                        Res.string.month_and_year,
                        mensualState.selectedMonth.month.enCastellano(),
                        mensualState.selectedMonth.year
                    ),
                    festivo = "",
                    onDateClick = {
                        //TODO: Hacer el diálogo!
                        //   viewModel.setSelectedMonth()
                    },
                    onFestivoClick = {},//No lo usamos.
                    onNextClick = { viewModel.nextMonth(1) },
                    onNextLongClick = { viewModel.nextMonth(12) },
                    onPrevClick = { viewModel.previousMonth(1) },
                    onPrevLongClick = { viewModel.previousMonth(12) }
                )
                MensualBody(
                    viewModel,
                    onFestivoClick = { festivoToast = it },
                    onMasDetallesClick = { date ->
                        //TODO: Navegar al hoy screen con la fecha.
                    },
                    onComjYLibraClick = { comjLibraToast = true },
                    onExcesosClick = { showExcesosDialog = true },
                )
            }
            FloatingActionButton(
                modifier = Modifier.padding(16.dp).align(Alignment.BottomEnd),
                onClick = { showEditDialog = true },
                contentColor = MaterialTheme.colorScheme.background,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Outlined.Add,
                    contentDescription = ""
                )
            }
        }

        //DIALOGOS y otros complementos:
        if(toastString != null){
            LongToast(toastString)
            viewModel.clearToasts()
        }
        if (toastId != null){
            LongToast(stringResource(toastId ?: Res.string._0))
            viewModel.clearToasts()
        }

        if (festivoToast != "") {
            LongToast(festivoToast)
            festivoToast = ""
        }
        if (comjLibraToast) {
            LongToast(TextoExplicComjsYLibras(mensualState.selectedCuDetalle))
            comjLibraToast = false
        }

        MyDialogInformation(
            show = showExcesosDialog,
            onDismiss = { showExcesosDialog = false },
            onConfirm = { showExcesosDialog = false },
            text = TextoExplicMermaGenerada(mensualState.selectedCuDetalle)
        )

        if (showEditDialog)
            DialogEditShift(
                hasShiftsShared = mensualState.iHaveShiftsShared,
                onDismiss = { showEditDialog = false },
                onBulkEditClick = {
                    showEditDialog = false
                    viewModel.onBulkEditClick()
                },
                onExcessClick = {
                    showEditDialog = false
                    viewModel.onExcessClick()
                },
                onEditClick = {
                    showEditDialog = false
                    viewModel.onEditClick()
                },
                onExchangeClick = {
                    showEditDialog = false
                    viewModel.onExchangeClick()
                })


    }
}

@Composable
fun MensualBody(
    viewModel: MensualViewModel,
    onFestivoClick: (String) -> Unit,
    onMasDetallesClick: (Int) -> Unit,
    onComjYLibraClick: () -> Unit,
    onExcesosClick: () -> Unit,
) {

    val mensualState by viewModel.mensualState.collectAsState(initial = MensualState())
    val primerDia = (mensualState.turnosDelSemanal.getOrNull(0)?.fecha?.dayOfWeek?.ordinal) ?: 0



    LazyVerticalGrid(GridCells.Fixed(7), modifier = Modifier.padding(horizontal = 16.dp)) {

        items(primerDia) {
            Box { }
        }

        items(mensualState.turnosDelSemanal) { turno ->
            MensualGridItem(turno) {
                viewModel.onDateSelected(turno)
            }
        }

        item(span = { GridItemSpan(7) }) {
            AnimatedVisibility(visible = mensualState.selectedDate != null) {
                MensualSelectedDate(
                    mensualState,
                    onFestivoClick = { onFestivoClick(mensualState.festivo ?: "") },
                    onMasDetallesClick = {
                        onMasDetallesClick(
                            mensualState.selectedDate?.toEpochDays() ?: -1
                        )
                    },
                    onComjYLibraClick = { onComjYLibraClick() },
                    onExcesosClick = { onExcesosClick() },
                )
            }
        }

    }
}