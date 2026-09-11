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
    primaryContainer = Color(0xFFD4F2E2),
    onPrimaryContainer = Color(0xFF002114),
    secondary = Color(0xFF4F6358),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD8E9DE),
    onSecondaryContainer = Color(0xFF0C1F16),
    tertiary = Color(0xFF75603A),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFE8B7),
    onTertiaryContainer = Color(0xFF281A00),
    background = Color(0xFFF7FAF7),
    onBackground = Color(0xFF181D19),
    surface = Color(0xFFF7FAF7),
    onSurface = Color(0xFF181D19),
    surfaceVariant = Color(0xFFE8EFEA),
    onSurfaceVariant = Color(0xFF414A45),
    outline = Color(0xFF717A74),
    outlineVariant = Color(0xFFC1CAC4),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF9DD6B9),
    onPrimary = Color(0xFF003824),
    primaryContainer = Color(0xFF005236),
    onPrimaryContainer = Color(0xFFD4F2E2),
    secondary = Color(0xFFB8CCBF),
    onSecondary = Color(0xFF23352B),
    secondaryContainer = Color(0xFF3A4D43),
    onSecondaryContainer = Color(0xFFD8E9DE),
    tertiary = Color(0xFFE8C77E),
    onTertiary = Color(0xFF3E2E09),
    tertiaryContainer = Color(0xFF5A471E),
    onTertiaryContainer = Color(0xFFFFE8B7),
    background = Color(0xFF101511),
    onBackground = Color(0xFFE1E5E1),
    surface = Color(0xFF101511),
    onSurface = Color(0xFFE1E5E1),
    surfaceVariant = Color(0xFF3F4943),
    onSurfaceVariant = Color(0xFFC1CAC4),
    outline = Color(0xFF8B958E),
    outlineVariant = Color(0xFF414A45),
)

@Composable
fun IslamicKnowledgeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = IslamicTypography,
        shapes = IslamicShapes,
        content = content,
    )
}
