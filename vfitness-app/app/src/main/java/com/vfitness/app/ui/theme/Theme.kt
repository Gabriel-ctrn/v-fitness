package com.vfitness.app.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme

@Composable
fun VFitnessTheme(content: @Composable () -> Unit) {
    val colorScheme = lightColorScheme(
        primary = Primary,
        secondary = Secondary,
        background = White,
        surface = White,
        onPrimary = White,
        onSecondary = White,
        onBackground = Black,
        onSurface = Black,
        tertiary = Accent
    )
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
