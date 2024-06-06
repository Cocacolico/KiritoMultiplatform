package es.kirito.kirito.turnos.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import es.kirito.kirito.core.presentation.theme.KiritoColors
import kirito.composeapp.generated.resources.Res
import kirito.composeapp.generated.resources.ic_label_24
import kirito.composeapp.generated.resources.plus1_COMJ
import kirito.composeapp.generated.resources.plus1_LIBRa
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun LabelComj(amount: Int){
    Box(modifier = Modifier.padding(horizontal = 12.dp), contentAlignment = Alignment.Center){
        Icon(painter = painterResource(Res.drawable.ic_label_24), contentDescription = "",
            tint = KiritoColors().COMJ, modifier = Modifier.graphicsLayer {
                this.scaleX = 2.2f
            }
        )
        Text(text = stringResource(Res.string.plus1_COMJ, amount), color = Color.White,
            modifier = Modifier.padding(end = 10.dp))
    }
}
@Composable
fun LabelLibra(amount: Int){
    Box(modifier = Modifier.padding(horizontal = 12.dp), contentAlignment = Alignment.Center){
        Icon(painter = painterResource(Res.drawable.ic_label_24), contentDescription = "",
            tint = KiritoColors().LIBRa, modifier = Modifier.graphicsLayer {
                this.scaleX = 2.2f
            }
        )
        Text(text = stringResource(Res.string.plus1_LIBRa, amount), color = Color.Black,
            modifier = Modifier.padding(end = 10.dp))
    }
}
