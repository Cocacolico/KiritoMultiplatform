@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)

package es.kirito.kirito.turnos.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import es.kirito.kirito.core.data.kiritoComponents.turnosKirito
import es.kirito.kirito.core.domain.util.colorDeFondoTurnos
import es.kirito.kirito.core.domain.util.colorTextoTurnos
import es.kirito.kirito.core.domain.util.enMiFormatoMedio
import es.kirito.kirito.core.domain.util.esTipoTurnoCambiable
import es.kirito.kirito.core.domain.util.nombreLargoTiposDeTurnos
import es.kirito.kirito.core.domain.util.toComposeColor
import es.kirito.kirito.core.presentation.components.HeaderWithBackAndHelp
import es.kirito.kirito.core.presentation.components.LongToast
import es.kirito.kirito.core.presentation.components.MyTextStd
import es.kirito.kirito.core.presentation.components.NotaAlPie
import es.kirito.kirito.core.presentation.components.ParagraphSubtitle
import es.kirito.kirito.core.presentation.components.dialogs.MyDialogInformation
import es.kirito.kirito.core.presentation.utils.noRippleClickable
import es.kirito.kirito.turnos.domain.EditarTurnoState
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources._0
import kirito.composeapp.generated.resources.ayuda
import kirito.composeapp.generated.resources.ayuda_editar_turno
import kirito.composeapp.generated.resources.cerrar
import kirito.composeapp.generated.resources.claves_o_tipos_101_7068_d_lz_rm
import kirito.composeapp.generated.resources.comj
import kirito.composeapp.generated.resources.compa_ero_con_el_que_has_cambiado_el_turno
import kirito.composeapp.generated.resources.compensaci_n_de_jornada
import kirito.composeapp.generated.resources.d_as_ganados
import kirito.composeapp.generated.resources.editar_turno
import kirito.composeapp.generated.resources.guardar
import kirito.composeapp.generated.resources.guardar_y_siguiente
import kirito.composeapp.generated.resources.libra
import kirito.composeapp.generated.resources.libranza_acordada
import kirito.composeapp.generated.resources.nombre_del_compa_ero
import kirito.composeapp.generated.resources.notas_del_turno
import kirito.composeapp.generated.resources.si_la_empresa_te_da_d_as_por_hacer_este_turno_se_lalo_aqu
import kirito.composeapp.generated.resources.tipo_de_turno
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun EditarTurnoScreen(navController: NavHostController) {

    val viewModel = koinViewModel<EditarTurnoViewModel>()
    val state by viewModel.state.collectAsState(EditarTurnoState())
    var showHelpDialog by remember { mutableStateOf(false) }

    val toastString by viewModel.toastString.collectAsState(null)
    val toastId by viewModel.toastId.collectAsState(null)

    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        EditarTurnoHeader(state,
            onBackRequested = {
                //TODO: PREGUNTAR si quieres irte cuando hay algo modificado. No marcharse así directamente.
                navController.navigateUp()
            },
            onHelpClicked = { showHelpDialog = true }
        )

        EditarTurnoBody(
            modifier = Modifier.weight(1f),
            state = state,
            onTurnoTextChanged = { texto -> viewModel.onTurnoTextChanged(texto) },
            onNextDayClick = { viewModel.onNextDayClick() },
            onCompiTextChanged = { texto -> viewModel.onCompiTextChanged(texto) },
            onShowDiasDebeClick = { viewModel.onShowDiasDebeClick() },
            onComjClick = { comj -> viewModel.onComjSelected(comj) },
            onLibraClick = { libra -> viewModel.onLibraSelected(libra) },
            onTipoSelected = { tipo -> viewModel.onTipoSelected(tipo) },
            onNotasChanged = { nota -> viewModel.onNotasChanged(nota) },
        )
        EditarTurnoFooter(
            onGuardarClick = { viewModel.onGuardarClick() },
            onGuardarYSiguienteClick = { viewModel.onGuardarYSiguienteClick() },
        )
    }


    if (toastString != null) {
        LongToast(toastString)
        viewModel.onToastLaunched()
    }
    if (toastId != null) {
        LongToast(stringResource(toastId ?: Res.string._0))
        viewModel.onToastLaunched()
    }

    MyDialogInformation(show = showHelpDialog, title = stringResource(Res.string.ayuda),
        text = stringResource(Res.string.ayuda_editar_turno),
        okText = stringResource(Res.string.cerrar),
        onConfirm = { showHelpDialog = false },
        onDismiss = { showHelpDialog = false })

    if(state.doneEditting){
        navController.navigateUp()
    }

}

@Composable
fun EditarTurnoHeader(
    state: EditarTurnoState,
    onBackRequested: () -> Unit,
    onHelpClicked: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        HeaderWithBackAndHelp(
            title = stringResource(Res.string.editar_turno),
            showHelp = true,
            onBackClick = { onBackRequested() },
            onHelpClick = { onHelpClicked() }
        )
        ParagraphSubtitle(
            text = state.selectedDate.enMiFormatoMedio(),
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.padding(bottom = 8.dp))
    }
}

@Composable
fun EditarTurnoBody(
    modifier: Modifier,
    state: EditarTurnoState,
    onTurnoTextChanged: (String) -> Unit,
    onNextDayClick: () -> Unit,
    onCompiTextChanged: (String) -> Unit,
    onShowDiasDebeClick: () -> Unit,
    onComjClick: (Int) -> Unit,
    onLibraClick: (Int) -> Unit,
    onTipoSelected: (String) -> Unit,
    onNotasChanged: (String) -> Unit,
) {
    var expandedNombreCompi by remember { mutableStateOf(false) }
    var expandedTipo by remember { mutableStateOf(false) }

    Column(modifier.verticalScroll(rememberScrollState())) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.editedShift.turno ?: "",
            onValueChange = { text -> onTurnoTextChanged(text) },
            label = { Text(stringResource(Res.string.claves_o_tipos_101_7068_d_lz_rm)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { onNextDayClick() })
        )

        Spacer(Modifier.padding(top = 8.dp))

        ExposedDropdownMenuBox(
            expanded = expandedTipo,
            onExpandedChange = {
                expandedTipo = true
            }
        ) {
            OutlinedTextField(
                value = "${state.editedShift.tipo} ${state.editedShift.tipo.nombreLargoTiposDeTurnos()}",
                readOnly = true,
                onValueChange = { text -> onCompiTextChanged(text) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipo)
                },
                placeholder = { MyTextStd(stringResource(Res.string.tipo_de_turno)) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(colorDeFondoTurnos(state.editedShift.tipo).toComposeColor()),
                textStyle = TextStyle.Default.copy(color = colorTextoTurnos(state.editedShift.tipo))
            )
            DropdownMenu(
                modifier = Modifier.exposedDropdownSize(),
                expanded = expandedTipo,
                onDismissRequest = { expandedTipo = false }
            ) {
                turnosKirito.forEach { tipo ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "$tipo ${tipo.nombreLargoTiposDeTurnos()}",
                                color = colorTextoTurnos(tipo),
                            )
                        },
                        modifier = Modifier.background(colorDeFondoTurnos(tipo).toComposeColor()),
                        onClick = {
                            onTipoSelected(tipo)
                            expandedTipo = false
                        }
                    )
                }
            }
        }

        AnimatedVisibility(state.editedShift.tipo.esTipoTurnoCambiable()) {
            Column {
                Spacer(Modifier.padding(top = 8.dp))
                MyTextStd(stringResource(Res.string.compa_ero_con_el_que_has_cambiado_el_turno))
                ExposedDropdownMenuBox(
                    expanded = expandedNombreCompi,
                    onExpandedChange = {}
                ) {
                    OutlinedTextField(
                        value = state.editedShift.nombreDebe,
                        onValueChange = { text ->
                            onCompiTextChanged(text)
                            expandedNombreCompi = true
                        },
                        label = { MyTextStd(stringResource(Res.string.nombre_del_compa_ero)) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedNombreCompi,
                        onDismissRequest = { expandedNombreCompi = false }
                    ) {
                        state.usuariosEnNombreDebe.forEach { nombre ->
                            DropdownMenuItem(
                                text = { MyTextStd(nombre) },
                                onClick = {
                                    onCompiTextChanged(nombre)
                                    expandedNombreCompi = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Card(Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 8.dp)) {
            Column(Modifier.fillMaxWidth().padding(start = 8.dp)) {
                Row(
                    Modifier
                        .noRippleClickable { onShowDiasDebeClick() }
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f).padding(vertical = 8.dp)) {
                        ParagraphSubtitle(
                            stringResource(Res.string.d_as_ganados),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        NotaAlPie(stringResource(Res.string.si_la_empresa_te_da_d_as_por_hacer_este_turno_se_lalo_aqu))
                    }
                    if (!state.showDiasDebe)
                        Icon(
                            imageVector = Icons.Outlined.ArrowDropDown, contentDescription = "",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                }

                AnimatedVisibility(state.showDiasDebe) {
                    Column {
                        FlowRow(verticalArrangement = Arrangement.Center) {
                            Column(Modifier.weight(1f)) {
                                MyTextStd(stringResource(Res.string.comj))
                                MyTextStd(
                                    stringResource(Res.string.compensaci_n_de_jornada),
                                    maxLines = 2
                                )
                            }
                            (0..3).forEach { option ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(option.toString())
                                    RadioButton(
                                        selected = option == state.editedShift.comj,
                                        onClick = { onComjClick(option) }
                                    )
                                }
                            }
                        }
                        FlowRow {
                            Column(Modifier.weight(1f)) {
                                MyTextStd(stringResource(Res.string.libra))
                                MyTextStd(
                                    stringResource(Res.string.libranza_acordada),
                                    maxLines = 2
                                )
                            }
                            (0..3).forEach { option ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(option.toString())
                                    RadioButton(
                                        selected = option == state.editedShift.libra,
                                        onClick = { onLibraClick(option) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        OutlinedTextField(
            value = state.editedShift.notas,
            onValueChange = { text -> onNotasChanged(text) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(Res.string.notas_del_turno)) },
            minLines = 5,
            maxLines = 10
        )
    }
}


@Composable
fun EditarTurnoFooter(
    onGuardarClick: () -> Unit,
    onGuardarYSiguienteClick: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = { onGuardarClick() }) {
            Text(
                stringResource(Res.string.guardar),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Button(
            onClick = { onGuardarYSiguienteClick() },
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(
                stringResource(Res.string.guardar_y_siguiente),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
