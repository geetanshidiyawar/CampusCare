package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CampusCareLightColorScheme =
  lightColorScheme(
    primary = BluePrimary,
    onPrimary = White,
    primaryContainer = BlueContainer,
    onPrimaryContainer = OnBlueContainer,
    secondary = BlueSecondary,
    onSecondary = White,
    secondaryContainer = Slate100,
    onSecondaryContainer = Slate900,
    tertiary = StatusRaisedAmber,
    onTertiary = White,
    background = Slate50,
    onBackground = Slate900,
    surface = White,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate700,
    outline = Slate200,
    outlineVariant = Slate300,
  )

private val CampusCareDarkColorScheme =
  darkColorScheme(
    primary = BlueSecondary,
    onPrimary = White,
    primaryContainer = OnBlueContainer,
    onPrimaryContainer = BlueContainer,
    secondary = BlueTertiary,
    onSecondary = White,
    background = Slate900,
    onBackground = Slate50,
    surface = Color(0xFF1E293B),
    onSurface = Slate50,
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Slate200,
    outline = Color(0xFF475569),
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep consistent campus branding
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) CampusCareDarkColorScheme else CampusCareLightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
