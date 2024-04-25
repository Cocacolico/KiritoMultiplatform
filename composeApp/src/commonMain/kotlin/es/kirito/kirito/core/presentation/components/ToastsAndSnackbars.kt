package es.kirito.kirito.core.presentation.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect



@Composable
fun ShortSnackbar(
    snackbarHostState: SnackbarHostState,
    message: String
) {
    LaunchedEffect(snackbarHostState) {
        snackbarHostState.showSnackbar(
            message = message,
            actionLabel = "Dismiss",
            duration = SnackbarDuration.Short
        )
    }

}