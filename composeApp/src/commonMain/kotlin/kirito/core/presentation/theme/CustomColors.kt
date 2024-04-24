package kirito.core.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CustomColors(
    val smallFABContainer: Color = Color.Unspecified,
    val colorSecondaryVariant: Color = Color.Unspecified,
    val fondoTareaPasada: Color = Color.Unspecified,
    val textoPocoImportante: Color = Color.Unspecified,
)

val OnLightCustomColors = CustomColors(
    smallFABContainer = azulKiritoOscuro,
    colorSecondaryVariant = amarilloKiritoSemi,
    fondoTareaPasada = amarilloKiritoTransparente,
    textoPocoImportante = Color(444444)
)

val OnDarkCustomColors = CustomColors(
    smallFABContainer = azulKiritoClaro,
    colorSecondaryVariant = amarilloKiritoSemi,
    fondoTareaPasada = amarilloKiritoTransparenteNight,
    textoPocoImportante = Color(808080)
)

val LocalCustomColors = staticCompositionLocalOf { CustomColors() }

//Esto es para hacer un atajo y no tener que escribir
//LocalCustomColors.current.smallFABContainer, sino directamente
//MaterialTheme.colorScheme.smallFABContainer.
val MaterialTheme.customColors: CustomColors
    @Composable
    @ReadOnlyComposable
    get() = LocalCustomColors.current