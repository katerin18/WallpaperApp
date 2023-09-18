package com.example.wallpaperapp.ui.theme

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

enum class CustomTheme {
    DARK, LIGHT
}

val lightBack = Color(0xFFE3DEEB)
val lightBackground = Color(0xfff3edf7)
val lightText = Color(0xFF44423D)

val darkBack = Color(0xFF3A4044)
val darkBackground = Color(0xFF472B34)
val darkText = Color(0xFFFBF8FC)

@Stable
class CustomColors(
    backgroundColor: Color,
    buttonBackgroundColor: Color,
    buttonTextColor: Color,
    textColor: Color,
    iconColor: Color
) {
    var backgroundColor by mutableStateOf(backgroundColor)
        private set
    var buttonBackgroundColor by mutableStateOf(buttonBackgroundColor)
        private set
    var buttonTextColor by mutableStateOf(buttonTextColor)
        private set
    var textColor by mutableStateOf(textColor)
        private set
    var iconColor by mutableStateOf(iconColor)
        private set

    fun update(colors: CustomColors) {
        this.backgroundColor = colors.backgroundColor
        this.buttonBackgroundColor = colors.buttonBackgroundColor
        this.buttonTextColor = colors.buttonTextColor
        this.textColor = colors.textColor
        this.iconColor = colors.iconColor
    }

    fun copy() = CustomColors(
        backgroundColor, buttonBackgroundColor, buttonTextColor, textColor, iconColor
    )
}