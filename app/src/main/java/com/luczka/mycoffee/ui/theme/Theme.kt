package com.luczka.mycoffee.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat

private val darkColorScheme = darkColorScheme(
//    primary = PrimaryDark,
//    secondary = SecondaryDark,
//    tertiary = TertiaryDark,
//    error = ErrorDark,
//    background = BackgroundDark,
//    outline = OutlineDark,
//    onPrimary = OnPrimaryDark,
//    onSecondary = OnSecondaryDark,
//    onTertiary = OnTertiaryDark,
//    onError = OnErrorDark,
//    onBackground = OnBackgroundDark,
//    primaryContainer = PrimaryContainerDark,
//    secondaryContainer = SecondaryContainerDark,
//    tertiaryContainer = TertiaryContainerDark,
//    errorContainer = ErrorContainerDark,
//    surface = SurfaceDark,
//    surfaceVariant = SurfaceVariantDark,
//    onPrimaryContainer = OnPrimaryContainerDark,
//    onSecondaryContainer = OnSecondaryContainerDark,
//    onTertiaryContainer = OnTertiaryContainerDark,
//    onErrorContainer = OnErrorContainerDark,
//    onSurface = OnSurfaceDark,
//    onSurfaceVariant = OnSurfaceVariantDark
)

private val lightColorScheme = lightColorScheme(
//    primary = PrimaryLight,
//    secondary = SecondaryLight,
//    tertiary = TertiaryLight,
//    error = ErrorLight,
//    background = BackgroundLight,
//    outline = OutlineLight,
//    onPrimary = OnPrimaryLight,
//    onSecondary = OnSecondaryLight,
//    onTertiary = OnTertiaryLight,
//    onError = OnErrorLight,
//    onBackground = OnBackgroundLight,
//    primaryContainer = PrimaryContainerLight,
//    secondaryContainer = SecondaryContainerLight,
//    tertiaryContainer = TertiaryContainerLight,
//    errorContainer = ErrorContainerLight,
//    surface = SurfaceLight,
//    surfaceVariant = SurfaceVariantLight,
//    onPrimaryContainer = OnPrimaryContainerLight,
//    onSecondaryContainer = OnSecondaryContainerLight,
//    onTertiaryContainer = OnTertiaryContainerLight,
//    onErrorContainer = OnErrorContainerLight,
//    onSurface = OnSurfaceLight,
//    onSurfaceVariant = OnSurfaceVariantLight
)

@Composable
fun MyCoffeeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        } else {
            if (darkTheme) darkColorScheme else lightColorScheme
        }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.apply {
                statusBarColor = colorScheme.background.toArgb()
                navigationBarColor = colorScheme.surfaceColorAtElevation(0.dp).toArgb()
            }
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}