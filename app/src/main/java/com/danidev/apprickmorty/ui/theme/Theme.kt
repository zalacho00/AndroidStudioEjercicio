package com.danidev.apprickmorty.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PortalGreen,
    onPrimary = Color.White,
    primaryContainer = PortalGreenContainer,
    onPrimaryContainer = OnPortalGreenContainer,
    secondary = DimensionCyan,
    onSecondary = Color.Black,
    secondaryContainer = DimensionCyanContainer,
    onSecondaryContainer = OnDimensionCyanContainer,
    tertiary = ToxicYellow,
    onTertiary = Color.Black,
    tertiaryContainer = ToxicYellowContainer,
    onTertiaryContainer = ToxicYellow,
    background = DarkBackground,
    onBackground = OnSurfaceLight,
    surface = DarkSurface,
    onSurface = OnSurfaceLight,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = OnSurfaceMedium,
    outline = CardBorderGreen.copy(alpha = 0.5f),
    outlineVariant = Color.White.copy(alpha = 0.12f),
    error = StatusDead,
    onError = Color.White
)

private val LightColorScheme = darkColorScheme(
    // We maintain the immersive dark portal experience for Rick & Morty
    primary = PortalGreen,
    onPrimary = Color.White,
    primaryContainer = PortalGreenContainer,
    onPrimaryContainer = OnPortalGreenContainer,
    secondary = DimensionCyan,
    onSecondary = Color.Black,
    secondaryContainer = DimensionCyanContainer,
    onSecondaryContainer = OnDimensionCyanContainer,
    tertiary = ToxicYellow,
    onTertiary = Color.Black,
    background = DarkBackground,
    onBackground = OnSurfaceLight,
    surface = DarkSurface,
    onSurface = OnSurfaceLight,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = OnSurfaceMedium,
    outline = CardBorderGreen.copy(alpha = 0.5f),
    outlineVariant = Color.White.copy(alpha = 0.12f),
    error = StatusDead,
    onError = Color.White
)

@Composable
fun ApprickmortyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Keep branded Rick & Morty dark portal aesthetic by default
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = DarkBackground.toArgb()
                window.navigationBarColor = DarkBackground.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}