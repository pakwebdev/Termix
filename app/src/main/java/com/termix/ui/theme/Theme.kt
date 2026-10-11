package com.termix.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TermixDark = darkColorScheme(
    primary = Color(0xFF3FB950),
    onPrimary = Color(0xFF0D1117),
    secondary = Color(0xFF58A6FF),
    background = Color(0xFF0D1117),
    surface = Color(0xFF161B22),
    onBackground = Color(0xFFE6EDF3),
    onSurface = Color(0xFFE6EDF3),
    surfaceVariant = Color(0xFF21262D),
    onSurfaceVariant = Color(0xFF8B949E),
)

@Composable
fun TermixTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TermixDark,
        content = content
    )
}
