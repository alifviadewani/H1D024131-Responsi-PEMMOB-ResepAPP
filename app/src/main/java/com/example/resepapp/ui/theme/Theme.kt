package com.example.resepapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = Orange40,
    primaryContainer = OrangeContainerLight,
    secondary = Green40,
    secondaryContainer = GreenContainerLight,
    background = SurfaceLight,
    surface = SurfaceLight
)

private val DarkColors = darkColorScheme(
    primary = Orange80,
    primaryContainer = OrangeContainerDark,
    secondary = Green80,
    secondaryContainer = GreenContainerDark,
    background = SurfaceDark,
    surface = SurfaceDark
)

@Composable
fun ResepAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        content = content
    )
}
