package com.fintrack.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF166B55),
    secondary = Color(0xFF4D6359),
    tertiary = Color(0xFF7B5800),
    surface = Color(0xFFFAFBF7),
    surfaceVariant = Color(0xFFE6EBE3),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF8AD8BD),
    secondary = Color(0xFFB5CCC1),
    tertiary = Color(0xFFEABC55),
    surface = Color(0xFF111411),
    surfaceVariant = Color(0xFF3F4944),
)

@Composable
fun FinTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
