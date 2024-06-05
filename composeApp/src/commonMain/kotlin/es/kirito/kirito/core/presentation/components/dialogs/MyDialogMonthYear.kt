package es.kirito.kirito.core.presentation.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.kirito.kirito.core.domain.models.MonthYear
import es.kirito.kirito.core.domain.util.enCastellano
import es.kirito.kirito.core.presentation.components.MyTextSubTitle
import es.kirito.kirito.core.presentation.components.Picker
import es.kirito.kirito.core.presentation.components.rememberPickerState
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.full_months
import kirito.composeapp.generated.resources.selecciona_un_mes
import kirito.composeapp.generated.resources.seleccionar
import kotlinx.datetime.Clock
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MyDialogMonthYear(
    show: Boolean,
    initialValue: MonthYear,
    okText: String = stringResource(Res.string.seleccionar),
    onDismiss: () -> Unit,
    onConfirm: (MonthYear) -> Unit
) {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val monthState = rememberPickerState(initialValue.month.enCastellano())
    val yearState = rememberPickerState(initialValue.year.toString())
    val months = stringArrayResource(Res.array.full_months)
    val years = (2019..(today.year + 1)).map { it.toString() }

    if (show)
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm(
                        MonthYear(
                            monthState.selectedItem.toMonth(months),
                            yearState.selectedItem.toInt()
                        )
                    )
                }) {
                    Text(okText)
                }
            },
            title = { MyTextSubTitle(text = stringResource(Res.string.selecciona_un_mes)) },
            text = {
                Row {
                    Picker(
                        state = monthState,
                        items = months,
                        startIndex = months.indexOf(monthState.selectedItem),
                        visibleItemsCount = 3,
                        modifier = Modifier.weight(0.3f),
                        textModifier = Modifier.padding(8.dp),
                        textStyle = TextStyle(fontSize = 20.sp)
                    )
                    Picker(
                        state = yearState,
                        items = years,
                        startIndex = years.indexOf(yearState.selectedItem),
                        visibleItemsCount = 3,
                        modifier = Modifier.weight(0.3f),
                        textModifier = Modifier.padding(8.dp),
                        textStyle = TextStyle(fontSize = 20.sp)
                    )
                }
            }
        )
}

private fun String.toMonth(months: List<String>): Month {
    val monthOrdinal = months.indexOf(this) + 1 //Recuerda que el índice es sobre 0.
    return Month(monthOrdinal)
}