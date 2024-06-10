package es.kirito.kirito.core.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp

fun Int.pxToDP(density: Density): Dp {
    val number = this
    return with(density) {
        number.toDp()
    }
}

@Composable
fun Int.pxToDP(): Dp {
    val density = LocalDensity.current
    val number = this
    return with(density) {
        number.toDp()
    }
}