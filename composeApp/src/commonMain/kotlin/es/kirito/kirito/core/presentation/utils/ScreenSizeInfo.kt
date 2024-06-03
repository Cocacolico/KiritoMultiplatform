package es.kirito.kirito.core.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

/** Getting screen size info for UI-related calculations */
data class ScreenSizeInfo(val hPX: Int, val wPX: Int, val hDP: Dp, val wDP: Dp)
enum class Orientation{LANDSCAPE, RECTANGLE, PORTRAIT, UNKNOWN}

@Composable
expect fun getScreenSizeInfo(): ScreenSizeInfo

fun ScreenSizeInfo.isLandscape(): Boolean {
    return this.hPX < this.wPX
}

fun ScreenSizeInfo.orientation(): Orientation {
    return when {
        this.hPX < this.wPX -> Orientation.LANDSCAPE
        this.hPX == this.wPX -> Orientation.RECTANGLE
        this.hPX > this.wPX -> Orientation.PORTRAIT
        else -> Orientation.UNKNOWN
    }
}