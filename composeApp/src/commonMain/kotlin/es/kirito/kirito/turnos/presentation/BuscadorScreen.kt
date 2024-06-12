@file:OptIn(KoinExperimentalAPI::class)

package es.kirito.kirito.turnos.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import es.kirito.kirito.core.data.database.GrGraficos
import es.kirito.kirito.core.domain.util.diasBooleansToString
import es.kirito.kirito.core.domain.util.fraseDeVigencia
import es.kirito.kirito.core.domain.util.fromInicialSemana
import es.kirito.kirito.core.domain.util.isEven
import es.kirito.kirito.core.domain.util.toInicialSemana
import es.kirito.kirito.core.presentation.components.DialogTitleText
import es.kirito.kirito.core.presentation.components.ExposedDropDownMenu
import es.kirito.kirito.core.presentation.components.ImagenEga
import es.kirito.kirito.core.presentation.components.LongToast
import es.kirito.kirito.core.presentation.components.MyTextPocoImportante
import es.kirito.kirito.core.presentation.components.dialogs.MyDialogImagenEga
import es.kirito.kirito.core.presentation.components.dialogs.TelefonosCompiDialog
import es.kirito.kirito.core.presentation.theme.azulKiritoTransparente
import es.kirito.kirito.turnos.domain.BuscadorState
import es.kirito.kirito.turnos.domain.models.Buscando
import es.kirito.kirito.turnos.domain.models.OrdenBusqueda
import es.kirito.kirito.turnos.domain.models.TurnoBuscadorDM
import es.kirito.kirito.turnos.presentation.components.TareasDeTurnoBuscador
import es.kirito.kirito.turnos.presentation.components.TurnoBuscadorHeader
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources._codigo_
import kirito.composeapp.generated.resources.cerrar
import kirito.composeapp.generated.resources.d_a_de_la_semana
import kirito.composeapp.generated.resources.filtrar
import kirito.composeapp.generated.resources.full_days
import kirito.composeapp.generated.resources.gr_fico
import kirito.composeapp.generated.resources.orden_nuevo_buscador
import kirito.composeapp.generated.resources.ordenar_por
import kirito.composeapp.generated.resources.teleindicador
import kirito.composeapp.generated.resources.teleindicadores
import kirito.composeapp.generated.resources.todos
import kirito.composeapp.generated.resources.tren
import kirito.composeapp.generated.resources.trenes
import kirito.composeapp.generated.resources.turno
import kirito.composeapp.generated.resources.turnos
import kirito.composeapp.generated.resources.veh_culo
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun BuscadorScreen(navController: NavHostController) {
    val viewModel = koinViewModel<BuscadorViewModel>()

    val state by viewModel.state.collectAsState(BuscadorState())
    var showFilterDialog by remember { mutableStateOf(false) }
    val toastString by viewModel.toast.collectAsState(null)
    val toastStringResource by viewModel.toastId.collectAsState(null)

    val selectTren =
        false//TODO: Esto viene de cuando desde otra screen queremos mostrar una cosa u otra. Arreglar.


    Surface(Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            BuscadorHeader(state, selectTren,
                onFilterClicked = { showFilterDialog = true },
                onToggleClicked = { index -> viewModel.onToggleClicked(index) },
                onTextChanged = { text -> viewModel.onTextChanged(text) }
            )

            BuscadorBody(
                state,
                onCompiClicked = { id -> viewModel.onCompiClicked(id) },
                onImageEgaClicked = { scrollValue, offsetX, turno ->
                    viewModel.onImageEgaClicked(scrollValue, offsetX, turno)
                }
            )
        }
        if (showFilterDialog)
            FilterSearchDialog(viewModel, onDismiss = { showFilterDialog = false }) {
                showFilterDialog = false
            }

        if (state.imagenEgaTurno != null)
            MyDialogImagenEga(
                horaOrigen = state.imagenEgaTurno?.horaOrigen ?: 0,
                horaFin = state.imagenEgaTurno?.horaFin ?: 0,
                tareasCortas = state.imagenEgaTurno?.listaTareasCortas ?: return@Surface,
                dia = Clock.System.todayIn(TimeZone.currentSystemDefault())
                    .toEpochDays()
                    .toLong(),
                coloresTrenes = state.imagenEgaTurno?.coloresTrenes,
                initialScroll = state.imagenEgaInitialScroll,
                factorZoom = 5,
            ) {
                viewModel.onDialogImagenEgaDimissed()
            }


        if (state.compi.id != -1L) {
            TelefonosCompiDialog(compi = state.compi, onCallClick = { phone ->
                viewModel.onCallClick(phone)
            }, onWhatsappClick = { phone ->
                viewModel.onWhatsappClick(phone)
            }, onDismiss = { viewModel.onCompiDismissed() })
        }
    }

    if (toastString != null){
        LongToast(toastString)
        viewModel.onToastLaunched()
    }
    if (toastStringResource != null){
        LongToast(stringResource(toastStringResource ?: return))
        viewModel.onToastLaunched()
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscadorHeader(
    state: BuscadorState,
    selectTren: Boolean,
    onFilterClicked: () -> Unit,
    onToggleClicked: (Int) -> Unit,
    onTextChanged: (String) -> Unit,
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val segmentedButtonOptions = listOf(
        stringResource(Res.string.turnos),
        stringResource(Res.string.trenes),
        stringResource(Res.string.teleindicadores),
    )
    var selectedButtonIndex by remember { mutableIntStateOf(if (selectTren) 1 else 0) }

    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.padding(
            horizontal = 16.dp,
            vertical = 8.dp
        )
    ) {
        segmentedButtonOptions.forEachIndexed { index, label ->
            SegmentedButton(
                selected = selectedButtonIndex == index,
                onClick = {
                    selectedButtonIndex = index
                    onToggleClicked(index)
                },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = segmentedButtonOptions.size
                ),
                colors = SegmentedButtonDefaults.colors().copy(
                    activeContainerColor = MaterialTheme.colorScheme.primary,
                    activeContentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = label,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = state.textfieldText,
            onValueChange = { onTextChanged(it) },
            singleLine = true,
            trailingIcon = {
                if (state.textfieldText.isNotBlank()) {
                    IconButton(onClick = { onTextChanged("") }) {
                        Icon(Icons.Default.Clear, contentDescription = null)
                    }
                }
            },
            keyboardActions = KeyboardActions(
                onSearch = { keyboardController?.hide() }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ), label = {
                Text(
                    text = when (state.buscando) {
                        Buscando.TURNOS -> stringResource(Res.string.turno)
                        Buscando.TRENES -> stringResource(Res.string.tren)
                        Buscando.TELEIND -> stringResource(Res.string.tren)
                    }
                )
            }
        )
        AnimatedVisibility(visible = state.buscando != Buscando.TELEIND) {
            Button(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = { onFilterClicked() }) {
                Text(text = stringResource(Res.string.filtrar))
            }
        }
    }
}


@Composable
fun BuscadorBody(
    state: BuscadorState,
    onCompiClicked: (Long) -> Unit,
    onImageEgaClicked: (Int, Float, TurnoBuscadorDM) -> Unit,
) {


    LazyColumn() {
        if (state.buscando == Buscando.TURNOS)
            items(
                state.turnosBuscados ?: emptyList(),
                key = { it.turno + it.idGrafico + it.diaSemana }) { turno ->
                val tareas = turno.listaTareas
                Card(Modifier.padding(horizontal = 16.dp)) {

                    TurnoBuscadorHeader(turno) { id ->
                        onCompiClicked(id)
                    }

                    ImagenEga(
                        timeInicio = turno.horaOrigen ?: 0,
                        timeFin = turno.horaFin ?: 0,
                        tareas = turno.listaTareasCortas ?: return@Card,
                        dia = null,
                        coloresTrenes = state.coloresTrenes,
                    ) { scrollValue, offsetX ->
                        onImageEgaClicked(scrollValue, offsetX, turno)
                    }

                    if (tareas?.isNotEmpty() == true)
                        TareasDeTurnoBuscador(tareas = tareas)
                }
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
            }
        if (state.buscando == Buscando.TRENES)
            items(
                state.trenesBuscados ?: emptyList(),
                key = { it.turno + it.idGrafico + it.diaSemana }) { turno ->
                val tareas = turno.listaTareas
                Card(Modifier.padding(horizontal = 16.dp)) {
                    TurnoBuscadorHeader(turno) { id -> onCompiClicked(id) }

                    ImagenEga(
                        timeInicio = turno.horaOrigen ?: 0,
                        timeFin = turno.horaFin ?: 0,
                        tareas = turno.listaTareasCortas ?: return@Card,
                        dia = null,
                        coloresTrenes = state.coloresTrenes,
                    ) { scrollValue, offsetX ->
                        onImageEgaClicked(scrollValue, offsetX, turno)
                    }

                    if (tareas?.isNotEmpty() == true)
                        TareasDeTurnoBuscador(tareas = tareas)
                }
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
            }
        if (state.buscando == Buscando.TELEIND) {
            item {
                Row {
                    Text(
                        text = stringResource(Res.string.tren),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(Res.string.veh_culo),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(Res.string._codigo_),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(Res.string.teleindicador),
                        modifier = Modifier.weight(1.5f),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            itemsIndexed(state.teleindicadoresBuscados) { index, teleind ->
                Row(
                    Modifier
                        .background(
                            color = if (index.isEven()) azulKiritoTransparente.copy(alpha = 0.1f) else Color.Transparent
                        )
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = teleind.tren,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = teleind.vehiculo,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = teleind.codigo,
                        modifier = Modifier.weight(1f),
                        maxLines = 2,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = diasBooleansToString(
                            teleind.lunes,
                            teleind.martes,
                            teleind.miercoles,
                            teleind.jueves,
                            teleind.viernes,
                            teleind.sabado,
                            teleind.domingo,
                            teleind.festivo
                        ), modifier = Modifier.weight(1.5f), textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}


@Composable
fun FilterSearchDialog(viewModel: BuscadorViewModel, onDismiss: () -> Unit, onApply: () -> Unit) {

    val state by viewModel.state.collectAsState(BuscadorState())


    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { DialogTitleText(titulo = stringResource(Res.string.filtrar)) },
        text = {
            SearchFilters(
                state.listaGraficos,
                state.selectedIdGrafico,
                state.selectedDiaSemana.fromInicialSemanaWithAll(),
                state.ordenBusqueda
            ) { selectedFilters ->
                viewModel.applySelectedFilters(selectedFilters)
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(Res.string.cerrar))
            }
        },
    )

}


private fun String.fromInicialSemanaWithAll(): Int {
    //La función fromInicialDiaSemana devuelve del 0 al 7, 8 es para toodo.
    return if (this == "todo") return 8 else this.fromInicialSemana()
}

@Composable
fun SearchFilters(
    listaGraficos: List<GrGraficos>?,
    selectedIdGrafico: Long,
    diaSemana: Int,
    selectedOrder: OrdenBusqueda,
    onSelected: (SelectedFilters) -> Unit
) {
    val listaDias = stringArrayResource(Res.array.full_days).toMutableList()
    listaDias.add(stringResource(Res.string.todos))
    val selectedFilters = SelectedFilters(
        idGrafico = selectedIdGrafico,
        diaSemana = diaSemana.toInicialSemana(),
        orden = selectedOrder
    )
    val indexSelectedGrafico = listaGraficos?.indexOfFirst { it.idGrafico == selectedIdGrafico }
        ?.coerceAtLeast(0)

    Column(Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            MyTextPocoImportante(
                text = stringResource(Res.string.ordenar_por),
                Modifier
                    .padding(end = 16.dp)
                    .weight(1f)
            )
            ExposedDropDownMenu(
                Modifier.weight(3f),
                stringArrayResource(Res.array.orden_nuevo_buscador).toList(),
                selectedOrder.ordinal
            ) { index ->
                selectedFilters.orden = when (index) {
                    0 -> OrdenBusqueda.CLAVE
                    1 -> OrdenBusqueda.H_INICIO
                    2 -> OrdenBusqueda.COMPANY
                    else -> OrdenBusqueda.CLAVE
                }
                onSelected(selectedFilters)
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        if (!listaGraficos.isNullOrEmpty())
            Row(verticalAlignment = Alignment.CenterVertically) {
                MyTextPocoImportante(
                    stringResource(Res.string.gr_fico),
                    Modifier
                        .padding(end = 16.dp)
                        .weight(1f)
                )
                if (listaGraficos.isNotEmpty())
                    ExposedDropDownMenu(Modifier.weight(3f), listaGraficos.map { grafico ->
                        (grafico.descripcion + " " + grafico.fraseDeVigencia())
                    }, indexSelectedGrafico ?: 0) { index ->
                        val idGrafico = listaGraficos[index].idGrafico
                        selectedFilters.idGrafico = idGrafico
                        onSelected(selectedFilters)
                    }
            }
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            MyTextPocoImportante(
                stringResource(Res.string.d_a_de_la_semana),
                Modifier
                    .padding(end = 16.dp)
                    .weight(1f)
            )
            ExposedDropDownMenu(Modifier.weight(3f), listaDias, diaSemana) {
                selectedFilters.diaSemana = it.toInicialSemana()
                onSelected(selectedFilters)
            }
        }
    }
}


