package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val SleekLightColorScheme = lightColorScheme(
    primary = PrimarySleek,
    onPrimary = OnPrimarySleek,
    primaryContainer = PrimaryContainerSleek,
    onPrimaryContainer = OnPrimaryContainerSleek,
    secondary = SecondarySleek,
    onSecondary = OnSecondarySleek,
    secondaryContainer = SecondaryContainerSleek,
    onSecondaryContainer = OnSecondaryContainerSleek,
    tertiary = TertiarySleek,
    onTertiary = OnTertiarySleek,
    tertiaryContainer = TertiaryContainerSleek,
    onTertiaryContainer = OnTertiaryContainerSleek,
    background = BackgroundSleek,
    onBackground = OnBackgroundSleek,
    surface = SurfaceSleek,
    onSurface = OnSurfaceSleek,
    surfaceVariant = SurfaceVariantSleek,
    onSurfaceVariant = OnSurfaceVariantSleek,
    outline = BorderSleek,
    outlineVariant = OutlineSleek
)

private val SleekDarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = OnBackgroundSleek,
    surface = OnBackgroundSleek,
    onBackground = BackgroundSleek,
    onSurface = BackgroundSleek
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is disabled by default to fully preserve the cohesive "Sleek Interface" branding
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> SleekDarkColorScheme
      else -> SleekLightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
