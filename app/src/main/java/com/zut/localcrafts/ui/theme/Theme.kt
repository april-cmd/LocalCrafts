package com.zut.localcrafts.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Silver,
    onPrimary = Ink,
    primaryContainer = GraphiteVariant,
    onPrimaryContainer = Silver,
    secondary = SilverSoft,
    onSecondary = Ink,
    tertiary = TealLight,
    onTertiary = Color(0xFF00262A),
    tertiaryContainer = TealContainerDark,
    onTertiaryContainer = OnTealContainerDark,
    background = Black,
    onBackground = Silver,
    surface = Graphite,
    onSurface = Silver,
    surfaceVariant = GraphiteVariant,
    onSurfaceVariant = SilverSoft,
    outline = GraphiteLine,
    outlineVariant = GraphiteLine,
    secondaryContainer = Color(0xFF2A2F36),
    onSecondaryContainer = Silver,
    surfaceContainerLowest = Color(0xFF0A0B0D),
    surfaceContainerLow = Color(0xFF131519),
    surfaceContainer = Graphite,
    surfaceContainerHigh = Color(0xFF1E2126),
    surfaceContainerHighest = Color(0xFF262A30)
)

private val LightColorScheme = lightColorScheme(
    primary = Charcoal,
    onPrimary = Color.White,
    primaryContainer = Fog,
    onPrimaryContainer = Ink,
    secondary = InkSoft,
    onSecondary = Color.White,
    tertiary = Teal,
    onTertiary = Color.White,
    tertiaryContainer = TealContainer,
    onTertiaryContainer = OnTealContainer,
    background = Mist,
    onBackground = Ink,
    surface = Color.White,
    onSurface = Ink,
    surfaceVariant = Fog,
    onSurfaceVariant = InkSoft,
    outline = Hairline,
    outlineVariant = Hairline,
    secondaryContainer = Color(0xFFE4E7EB),
    onSecondaryContainer = Ink,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF8F9FA),
    surfaceContainer = Color(0xFFF1F3F5),
    surfaceContainerHigh = Fog,
    surfaceContainerHighest = Color(0xFFE4E7EB)
)

@Composable
fun LocalCraftsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Dynamic colour is removed on purpose: on Android 12+ it replaces our
    // palette with the phone's wallpaper colours, so the app would not look
    // like LocalCrafts. We always use our own charcoal and white palette instead.
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

/** Text field colours shared by the artisan screens: neutral outline, teal accent when focused. */
@Composable
fun craftFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.tertiary,
    focusedLabelColor = MaterialTheme.colorScheme.tertiary,
    cursorColor = MaterialTheme.colorScheme.tertiary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    unfocusedContainerColor = MaterialTheme.colorScheme.surface
)
