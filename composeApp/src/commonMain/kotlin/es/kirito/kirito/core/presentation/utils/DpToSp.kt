package es.kirito.kirito.core.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp



/**
 * Devuelve como si fueran Scaled Pixels el tamaño que sería en DP. Útil para tamaños fijos.
 * */
@Composable
fun Dp.dpToSp() = with(LocalDensity.current) { this@dpToSp.toSp() }

/**
 * Devuelve como si fueran Scaled Pixels el tamaño que sería en DP. Útil para tamaños fijos.
 * */

fun Dp.dpToSp(density: Density) = with(density) { this@dpToSp.toSp() }


/**
 * Devuelve un Int como si fueran Scaled Pixels el tamaño que sería en DP. Útil para tamaños fijos.
 * */
fun Int.toFalseSp(density: Density) = with(density) {
    val dps = this@toFalseSp.dp
    return@with dps.dpToSp(density)
}