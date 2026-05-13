package com.fintrack.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.fintrack.domain.model.Settings
import kotlinx.coroutines.flow.Flow

private val LightColors = lightColorScheme(
    primary = Color(0xFF1B6B58),
    onPrimary = Color(0xFFF7FFFB),
    primaryContainer = Color(0xFFD8F1E8),
    onPrimaryContainer = Color(0xFF12392F),
    secondary = Color(0xFF4A635C),
    onSecondary = Color(0xFFF4FBF7),
    tertiary = Color(0xFF7A5E28),
    background = Color(0xFFF3F5F2),
    onBackground = Color(0xFF161C19),
    surface = Color(0xFFFCFDFC),
    onSurface = Color(0xFF161C19),
    surfaceVariant = Color(0xFFE7ECE7),
    onSurfaceVariant = Color(0xFF53605A),
    outline = Color(0xFFD3DAD4),
    error = Color(0xFFB93831),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF8ED9C0),
    onPrimary = Color(0xFF09352A),
    primaryContainer = Color(0xFF14473A),
    onPrimaryContainer = Color(0xFFD8F1E8),
    secondary = Color(0xFFB2CBC2),
    onSecondary = Color(0xFF18342D),
    tertiary = Color(0xFFE7C780),
    background = Color(0xFF101513),
    onBackground = Color(0xFFE6ECE8),
    surface = Color(0xFF171D1A),
    onSurface = Color(0xFFE6ECE8),
    surfaceVariant = Color(0xFF28302C),
    onSurfaceVariant = Color(0xFFB8C4BE),
    outline = Color(0xFF3B4541),
    error = Color(0xFFFF8B83),
)

private val FinTrackTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 34.sp,
        lineHeight = 40.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 23.sp,
        lineHeight = 29.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,
        lineHeight = 23.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 23.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 18.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
    ),
)

@Composable
fun FinTrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = FinTrackTypography,
        content = content,
    )
}

@Composable
fun FinTrackThemeHost(
    settings: Flow<Settings>,
    content: @Composable () -> Unit,
) {
    val currentSettings by settings.collectAsState(initial = Settings())
    FinTrackTheme(
        darkTheme = currentSettings.darkModeEnabled,
        content = content,
    )
}
