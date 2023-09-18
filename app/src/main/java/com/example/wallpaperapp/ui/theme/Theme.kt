package com.example.wallpaperapp.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

private val CustomDarkColors = CustomColors(
    backgroundColor = darkBackground,
    buttonBackgroundColor = darkBack,
    buttonTextColor = darkText,
    textColor = darkText,
    iconColor = darkText
)

private val CustomLightColors = CustomColors(
    backgroundColor = lightBackground,
    buttonBackgroundColor = lightBack,
    buttonTextColor = lightText,
    textColor = lightText,
    iconColor = lightText
)

private val LocalColorsProvider = staticCompositionLocalOf {
    CustomLightColors
}

@Composable
private fun CustomLocalProvider(
    colors: CustomColors,
    content: @Composable() () -> Unit
) {
    val colorPalette = remember {
        colors.copy()
    }
    colorPalette.update(colors)
    CompositionLocalProvider(LocalColorsProvider provides colorPalette, content = content)
}

private val CustomTheme.colors: Pair<ColorScheme, CustomColors>
    get() = when (this) {
        CustomTheme.DARK -> darkColorScheme() to CustomDarkColors
        CustomTheme.LIGHT -> lightColorScheme() to CustomLightColors
    }

object CustomThemeManager {
    val colors: CustomColors
        @Composable
        get() = LocalColorsProvider.current

    var customTheme by mutableStateOf(CustomTheme.LIGHT)
}

@Composable
fun CustomJCTheme(
    customTheme: CustomTheme = CustomThemeManager.customTheme,
    content: @Composable () -> Unit
) {
    val (colorPalette, lcColor) = customTheme.colors
    CustomLocalProvider(colors = lcColor) {
        MaterialTheme(
            colorScheme = colorPalette,
            typography = Typography,
            content = content,
            shapes = Shapes()
        )
    }
}