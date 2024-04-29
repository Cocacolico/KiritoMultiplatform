package es.kirito.kirito.core.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun MyIconError() {
    Icon(
        Icons.Filled.Warning,
        contentDescription = "error",
        tint = MaterialTheme.colorScheme.error
    )
}