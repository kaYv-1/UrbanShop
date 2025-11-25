package com.example.prueba_n2.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = ElectricBlue,
    secondary = MediumGray,
    background = Color.Black, // Fondo completamente negro para modo oscuro
    surface = Charcoal,
    onPrimary = OffWhite,
    onSecondary = OffWhite,
    onBackground = OffWhite,
    onSurface = OffWhite,
    error = Color.Red
)

private val LightColorScheme = lightColorScheme(
    primary = ElectricBlue,
    secondary = DarkGray,
    background = OffWhite,
    surface = Color.White,
    onPrimary = OffWhite,
    onSecondary = OffWhite,
    onBackground = Charcoal,
    onSurface = Charcoal,
    error = Color.Red
)

@Composable
fun Prueba_n2Theme(
    darkTheme: Boolean = true, // Forzar modo oscuro por defecto
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb() // Status bar del color del fondo
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
