package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val SlateDarkColorScheme = darkColorScheme(
  primary = BurntOrange,
  onPrimary = TextWhite,
  primaryContainer = BurntOrangeDark,
  onPrimaryContainer = TextWhite,
  secondary = MoneyInGreen,
  onSecondary = TextWhite,
  tertiary = MoneyOutBlue,
  onTertiary = TextWhite,
  background = SlateBackground,
  onBackground = TextWhite,
  surface = SlateSurface,
  onSurface = TextWhite,
  surfaceVariant = SlateSurfaceVariant,
  onSurfaceVariant = TextMuted,
  outline = SlateBorder,
  error = OverdueRed
)

@Composable
fun FunktionTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = SlateDarkColorScheme,
    typography = Typography,
    content = content
  )
}

