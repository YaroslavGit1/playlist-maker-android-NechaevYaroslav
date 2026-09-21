package com.example.playlist_maker_android_nechaevyaroslav.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class PlaylistColors(
    val background: Color,
    val onBackground: Color,
    val secondary: Color,
    val field: Color,
    val onField: Color,
    val accent: Color,
    val onAccent: Color,
)

private val LightPlaylistColors = PlaylistColors(
    background = White,
    onBackground = Black,
    secondary = Gray,
    field = LightGray,
    onField = Gray,
    accent = Blue,
    onAccent = White,
)

private val DarkPlaylistColors = PlaylistColors(
    background = Black,
    onBackground = White,
    secondary = White,
    field = White,
    onField = Black,
    accent = Blue,
    onAccent = White,
)

val LocalPlaylistColors = staticCompositionLocalOf { LightPlaylistColors }

val LocalDarkTheme = staticCompositionLocalOf { false }

@Composable
fun PlaylistmakerandroidNechaevYaroslavTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkPlaylistColors else LightPlaylistColors
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = colors.accent,
            onPrimary = colors.onAccent,
            background = colors.background,
            onBackground = colors.onBackground,
            surface = colors.background,
            onSurface = colors.onBackground,
            surfaceVariant = colors.field,
            onSurfaceVariant = colors.secondary,
        )
    } else {
        lightColorScheme(
            primary = colors.accent,
            onPrimary = colors.onAccent,
            background = colors.background,
            onBackground = colors.onBackground,
            surface = colors.background,
            onSurface = colors.onBackground,
            surfaceVariant = colors.field,
            onSurfaceVariant = colors.secondary,
        )
    }

    CompositionLocalProvider(
        LocalPlaylistColors provides colors,
        LocalDarkTheme provides darkTheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
        )
    }
}
