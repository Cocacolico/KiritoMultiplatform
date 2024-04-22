package es.kirito.kirito.android.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Material 3 color schemes
private val kiritoLightColorScheme = lightColorScheme(
    primary = kiritoLightPrimary,
    onPrimary = kiritoLightOnPrimary,
    primaryContainer = kiritoLightPrimaryContainer,
    onPrimaryContainer = kiritoLightOnPrimaryContainer,
    inversePrimary = kiritoLightPrimaryInverse,
    secondary = kiritoLightSecondary,
    onSecondary = kiritoLightOnSecondary,
    secondaryContainer = kiritoLightSecondaryContainer,
    onSecondaryContainer = kiritoLightOnSecondaryContainer,
    tertiary = kiritoLightTertiary,
    onTertiary = kiritoLightOnTertiary,
    tertiaryContainer = kiritoLightTertiaryContainer,
    onTertiaryContainer = kiritoLightOnTertiaryContainer,
    error = kiritoLightError,
    onError = kiritoLightOnError,
    errorContainer = kiritoLightErrorContainer,
    onErrorContainer = kiritoLightOnErrorContainer,
    background = kiritoLightBackground,
    onBackground = kiritoLightOnBackground,
    surface = kiritoLightSurface,
    onSurface = kiritoLightOnSurface,
    inverseSurface = kiritoLightInverseSurface,
    inverseOnSurface = kiritoLightInverseOnSurface,
    surfaceVariant = kiritoLightSurfaceVariant,
    onSurfaceVariant = kiritoLightOnSurfaceVariant,
    outline = kiritoLightOutline
)

private val kiritoDarkColorScheme = darkColorScheme(
    primary = azulKirito,
    onPrimary = Color.Black,
    primaryContainer = kiritoDarkPrimaryContainer,
    onPrimaryContainer = kiritoDarkOnPrimaryContainer,
  //  inversePrimary = kiritoDarkPrimaryInverse,//Vamos a no meterlo.
    secondary = amarilloKirito,
    onSecondary = Color.Black,
    secondaryContainer = azulKiritoTransparente,
    onSecondaryContainer = kiritoDarkOnSecondaryContainer,
    tertiary = Color.White,
    onTertiary = amarilloKiritoClaro98,
    tertiaryContainer = kiritoDarkTertiaryContainer,
    onTertiaryContainer = kiritoDarkOnTertiaryContainer,
    error = kiritoDarkError,
    onError = kiritoDarkOnError,
    errorContainer = kiritoDarkErrorContainer,
    onErrorContainer = kiritoDarkOnErrorContainer,
    background = kiritoDarkBackground,
    onBackground = kiritoDarkOnBackground,
    surface = kiritoDarkSurface,
    onSurface = kiritoDarkOnSurface,
    inverseSurface = kiritoDarkInverseSurface,
    inverseOnSurface = kiritoDarkInverseOnSurface,
    surfaceVariant = kiritoDarkSurfaceVariant,
    onSurfaceVariant = kiritoDarkOnSurfaceVariant,
    outline = kiritoDarkOutline
)

@Composable
fun KiritoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val kiritoColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> kiritoDarkColorScheme
        else -> kiritoLightColorScheme
    }
    val customColors = if (darkTheme) OnDarkCustomColors else OnLightCustomColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = azulKiritoOscuro.toArgb()
            // window.navigationBarColor //Otras cosas que se pueden editar aquí...
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =  false //darkTheme
        }
    }
    CompositionLocalProvider (
        LocalCustomColors provides customColors
    ){
        MaterialTheme(
            colorScheme = kiritoColorScheme,
            typography = kiritoTypography,
            shapes = shapes,
            content = content
        )
    }
}