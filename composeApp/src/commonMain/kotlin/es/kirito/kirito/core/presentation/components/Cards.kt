package es.kirito.kirito.core.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import es.kirito.kirito.core.domain.util.toComposeColor
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource


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
@Composable
fun DiasEspecialesCard(text: StringResource, cantidad: String, color: String) {
    Card(
        colors = cardColors(
            containerColor = color.toComposeColor()
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(4.dp)
        ) {
            Text(
                text = stringResource(text,cantidad),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 2.dp)
            )
        }

}