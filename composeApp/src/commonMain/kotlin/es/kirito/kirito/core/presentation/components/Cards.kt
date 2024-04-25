package es.kirito.kirito.core.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ErrorCard(text: String) {
    Card(
        colors =
        cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.error
        ),
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Text(text = text, Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
    }
}