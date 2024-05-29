package es.kirito.kirito.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.kirito.kirito.core.presentation.theme.KiritoColors
import es.kirito.kirito.core.presentation.theme.Orange
import es.kirito.kirito.core.presentation.theme.azulKirito
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.festivo
import org.jetbrains.compose.resources.stringResource


@Composable
fun HeaderWithPrevNext(
    title: String,
    festivo: String,
    onDateClick: () -> Unit,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    onPrevLongClick: () -> Unit,
    onNextLongClick: () -> Unit,
    onFestivoClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        MyIconButtonWLClick(
            onClick = onPrevClick,
            onLongClick = onPrevLongClick,
            modifier = Modifier
                .padding(12.dp)
                .wrapContentWidth()
        ) {
            Icon(
                Icons.AutoMirrored.Outlined.ArrowBackIos,
                contentDescription = "",
                modifier = Modifier
                    .background(color = azulKirito, shape = CircleShape)
                    .padding(8.dp), tint = MaterialTheme.colorScheme.background
            )
        }
        AutoResizeText(
            text = title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = azulKirito,
            fontSizeRange = FontSizeRange(14.sp, 22.sp, 2.sp),
            modifier = Modifier
                .weight(1f)
                .clickable{ onDateClick() }
                .padding(vertical = 8.dp),
            maxLines = 2,
        )
        if (festivo != "")
            Text(
                text = stringResource(Res.string.festivo),
                color = Color.Black,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { onFestivoClick() }
                    .background(color = KiritoColors().FESTIVO)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )



        MyIconButtonWLClick(
            onClick = onNextClick,
            onLongClick = onNextLongClick,
            modifier = Modifier
                .padding(12.dp)
                .wrapContentWidth()
        ) {
            Icon(
                Icons.AutoMirrored.Outlined.ArrowForwardIos,
                contentDescription = "",
                modifier = Modifier
                    .background(color = azulKirito, shape = CircleShape)
                    .padding(8.dp),
                tint = MaterialTheme.colorScheme.background
            )
        }
    }
}