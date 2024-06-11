package es.kirito.kirito.core.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.kirito.kirito.core.presentation.theme.amarilloKirito
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyIconButtonWLClick(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .size(40.dp)
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.primary)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
                role = Role.Button
            ),
        contentAlignment = Alignment.Center
    ) {
        val contentColor = MaterialTheme.colorScheme.onPrimary
        CompositionLocalProvider(LocalContentColor provides contentColor, content = content)
    }
}

@Composable
fun ButtonMenuPrincipal(
    onClick: () -> Unit,
    icon: ImageVector,
    text: StringResource,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(6.dp)
            .clip(shape = RoundedCornerShape(20))
            .clickable(onClick = onClick)
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .size(height = 95.dp, width = 110.dp)
            .fillMaxSize()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier
                .size(40.dp)
        )
        Text(
            text = stringResource(text),
            fontSize = 12.sp,
            maxLines = 2,
            lineHeight = 10.sp,
            overflow = TextOverflow.Visible,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            //modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ButtonMenuPrincipalBadge(
    onClick: () -> Unit,
    icon: ImageVector,
    text: StringResource,
    numNotificaciones: Int = 0,
    modifier: Modifier = Modifier,
) {
    BadgedBox(
        badge = {
            if (numNotificaciones > 0)
                Badge(
                    containerColor = amarilloKirito,
                    contentColor = Color.Black,
                ) {
                    Text(
                        fontSize = 14.sp,
                        text = numNotificaciones.toString()
                    )
                }
        },
        modifier = modifier
            .padding(6.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .clip(shape = RoundedCornerShape(20))
                .clickable(onClick = onClick)
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
                .size(height = 95.dp, width = 110.dp)
                .fillMaxSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = modifier
                    .size(40.dp)
            )
            Text(
                text = stringResource(text),
                fontSize = 12.sp,
                maxLines = 2,
                lineHeight = 10.sp,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Visible,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
@Composable
fun ButtonMenuPrincipalBadgeDiasIniciales(
    onClick: () -> Unit,
    icon: ImageVector,
    text: StringResource,
    numNotificaciones: Int = 0,
    modifier: Modifier = Modifier,
) {
    BadgedBox(
        badge = {
            if (numNotificaciones == 0)
                Badge(
                    containerColor = amarilloKirito,
                    contentColor = Color.Black,
                ) {
                    Text(
                        fontSize = 14.sp,
                        text = "*"
                    )
                }
        },
        modifier = modifier
            .padding(6.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .clip(shape = RoundedCornerShape(20))
                .clickable(onClick = onClick)
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
                .size(height = 95.dp, width = 110.dp)
                .fillMaxSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = modifier
                    .size(40.dp)
            )
            Text(
                text = stringResource(text),
                fontSize = 12.sp,
                maxLines = 2,
                lineHeight = 10.sp,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Visible,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}