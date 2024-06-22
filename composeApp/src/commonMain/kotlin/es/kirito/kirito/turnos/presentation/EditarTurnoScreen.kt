@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)

package es.kirito.kirito.turnos.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
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
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavHostController
import es.kirito.kirito.core.data.kiritoComponents.turnosKirito
import es.kirito.kirito.core.domain.util.enMiFormatoMedio
import es.kirito.kirito.core.domain.util.esTipoTurnoCambiable
import es.kirito.kirito.core.domain.util.nombreLargoTiposDeTurnos
import es.kirito.kirito.core.presentation.components.HeaderWithBack
import es.kirito.kirito.core.presentation.components.MyTextStd
import es.kirito.kirito.core.presentation.components.NotaAlPie
import es.kirito.kirito.core.presentation.components.ParagraphSubtitle
import es.kirito.kirito.core.presentation.components.ParagraphTitle
import es.kirito.kirito.turnos.domain.EditarTurnoState
import kirito.composeapp.generated.resources.Res
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

    Column(Modifier.fillMaxWidth()) {
        HeaderWithBack(stringResource(Res.string.editar_turno)) {
            //TODO: PREGUNTAR si quieres irte cuando hay algo modificado. No marcharse así directamente.
            navController.navigateUp()
        }
        EditarTurnoBody(
            state = state,
            onTurnoTextChanged = { texto -> viewModel.onTextChanged(texto) },
            onNextDayClick = { viewModel.onNextDayClick() },
            onCompiTextChanged = { texto -> viewModel.onCompiTextChanged(texto) },
            onShowDiasDebeClick = { viewModel.onShowDiasDebeClick() },
            onComjClick = { comj -> viewModel.onComjSelected(comj) },
            onLibraClick = { libra -> viewModel.onLibraSelected(libra) },
            onTipoSelected = {tipo -> viewModel.onTipoSelected(tipo)},
            onNotasChanged = {nota -> viewModel.onNotasChanged(nota)},
        )
        EditarTurnoFooter(
            onGuardarClick = { viewModel.onGuardarClick() },
            onGuardarYSiguienteClick = { viewModel.onGuardarYSiguienteClick() },
        )
    }

}

@Composable
fun EditarTurnoBody(
    state: EditarTurnoState,
    onTurnoTextChanged: (String) -> Unit,
    onNextDayClick: () -> Unit,
    onCompiTextChanged: (String) -> Unit,
    onShowDiasDebeClick: () -> Unit,
    onComjClick: (Int) -> Unit,
    onLibraClick: (Int) -> Unit,
    onTipoSelected: (String) ->Unit,
    onNotasChanged: (String)-> Unit,
    ) {
    var expandedNombreCompi by remember { mutableStateOf(false) }
    var expandedTipo by remember { mutableStateOf(false) }


    ParagraphSubtitle(state.selectedDate.enMiFormatoMedio())
    Column(Modifier.verticalScroll(rememberScrollState())) {
        OutlinedTextField(
            value = "",
            onValueChange = { text -> onTurnoTextChanged(text) },
            placeholder = { Text(stringResource(Res.string.claves_o_tipos_101_7068_d_lz_rm)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { onNextDayClick() })
        )

        ExposedDropdownMenuBox(
            expanded = expandedTipo,
            onExpandedChange = {
                expandedTipo = true
            }
        ) {
            androidx.compose.material3.OutlinedTextField(
                value = state.textNombreDebe,
                onValueChange = { text -> onCompiTextChanged(text) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipo)
                },
                placeholder = { MyTextStd(stringResource(Res.string.tipo_de_turno)) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expandedTipo,
                onDismissRequest = { expandedTipo = false }
            ) {
                turnosKirito.forEach { tipo ->
                    DropdownMenuItem(
                        text = { MyTextStd("$tipo ${tipo.nombreLargoTiposDeTurnos()}") },
                        onClick = {
                            onTipoSelected(tipo)
                            expandedTipo = false
                        }
                    )
                }
            }
        }

        if (state.selectedShift.tipo.esTipoTurnoCambiable()) {
            MyTextStd(stringResource(Res.string.compa_ero_con_el_que_has_cambiado_el_turno))
            ExposedDropdownMenuBox(
                expanded = expandedNombreCompi,
                onExpandedChange = {}
            ) {
                androidx.compose.material3.OutlinedTextField(
                    value = state.textNombreDebe,
                    onValueChange = { text ->
                        onCompiTextChanged(text)
                        expandedNombreCompi = true
                    },
                    placeholder = { MyTextStd(stringResource(Res.string.nombre_del_compa_ero)) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
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

        Column(Modifier.clickable { onShowDiasDebeClick() }.fillMaxWidth()) {
            ParagraphTitle(stringResource(Res.string.d_as_ganados))
            NotaAlPie(stringResource(Res.string.si_la_empresa_te_da_d_as_por_hacer_este_turno_se_lalo_aqu))
        }
        if (state.showDiasDebe) {
            FlowRow {
                Column(Modifier.weight(1f)) {
                    MyTextStd(stringResource(Res.string.comj))
                    MyTextStd(stringResource(Res.string.compensaci_n_de_jornada))
                }
                (0..3).forEach { option ->
                    RadioButton(
                        selected = option == state.selectedShift.comj,
                        onClick = { onComjClick(option) }
                    )
                }
            }
            FlowRow {
                Column(Modifier.weight(1f)) {
                    MyTextStd(stringResource(Res.string.libra))
                    MyTextStd(stringResource(Res.string.libranza_acordada))
                }
                (0..3).forEach { option ->
                    RadioButton(
                        selected = option == state.selectedShift.libra,
                        onClick = { onLibraClick(option) }
                    )
                }
            }
        }

        androidx.compose.material3.OutlinedTextField(
            value = state.editedShift.notas,
            onValueChange = {text -> onNotasChanged(text)},
            modifier = Modifier.fillMaxWidth(),
            minLines = 5,
            maxLines = 10,
        )



    }


}


@Composable
fun EditarTurnoFooter(
    onGuardarClick: () -> Unit,
    onGuardarYSiguienteClick: () -> Unit,
) {
    Row {
        Button(onClick = { onGuardarClick() }) {
            Text(stringResource(Res.string.guardar))
        }
        Button(onClick = { onGuardarYSiguienteClick() }) {
            Text(stringResource(Res.string.guardar_y_siguiente))
        }
    }
}
