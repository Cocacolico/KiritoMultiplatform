package kirito.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.button_aceptar
import kirito.composeapp.generated.resources.cancelar
import kirito.composeapp.generated.resources.selecciona_una_hora
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (LocalTime) -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    state: TimePickerState,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = containerColor
                ),
            color = containerColor
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = stringResource(Res.string.selecciona_una_hora),
                    style = MaterialTheme.typography.labelMedium
                )
                TimePicker(state = state)
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = { onDismissRequest() }) {
                        Text(text = stringResource(Res.string.cancelar))
                    }
                    TextButton(onClick = { onConfirm(state.toLocalTime()) }) {
                        Text(text = stringResource(Res.string.button_aceptar))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun TimePickerState.toLocalTime(): LocalTime {
    val seconds = this.hour * 3600 + this.minute * 60
    return LocalTime.fromSecondOfDay(seconds)
}
