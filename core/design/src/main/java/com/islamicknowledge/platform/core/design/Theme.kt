package com.islamicknowledge.platform.core.design

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF176B4D),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB7F1D2),
    onPrimaryContainer = Color(0xFF002114),
    secondary = Color(0xFF4D6357),
    background = Color(0xFFF9FBF8),
    surface = Color(0xFFF9FBF8)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9DD6B9),
    onPrimary = Color(0xFF003824),
    primaryContainer = Color(0xFF005236),
    onPrimaryContainer = Color(0xFFB7F1D2),
    secondary = Color(0xFFB4CCBE),
    background = Color(0xFF101411),
    surface = Color(0xFF101411)
)

@Composable
fun IslamicKnowledgeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = IslamicTypography,
        shapes = IslamicShapes,
        content = content
    )
}
