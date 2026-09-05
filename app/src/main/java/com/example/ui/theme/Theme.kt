package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.data.AppThemeColor

@Composable
fun CleanGoldTheme(
    themeColor: AppThemeColor = AppThemeColor.GOLD,
    content: @Composable () -> Unit
) {
    val colorScheme = darkColorScheme(
        primary = themeColor.primary,
        onPrimary = Color.Black,
        primaryContainer = themeColor.primaryVariant,
        onPrimaryContainer = Color.White,
        secondary = themeColor.glow,
        onSecondary = Color.White,
        background = DarkBackground,
        onBackground = Color.White,
        surface = DarkSurface,
        onSurface = Color.White,
        surfaceVariant = DarkCard,
        onSurfaceVariant = Color(0xFFCCCCCC)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatibility alias for tests
@Composable
fun MyApplicationTheme(content: @Composable () -> Unit) {
    CleanGoldTheme(themeColor = AppThemeColor.GOLD, content = content)
}
