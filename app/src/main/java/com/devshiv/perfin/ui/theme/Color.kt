package com.devshiv.perfin.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun Color.toHex(): String {
    val argb = this.toArgb()
    return String.format("#%06X", 0xFFFFFF and argb)
}

val PrimaryGreen = Color(0xFF2BB673)
val LightGreen = Color(0xFF8EE4AF)
val DarkGreen = Color(0xFF1E7F5A)

val Gold = Color(0xFFF5C045)

val Background = Color(0xFFF5F7F6)
val CardBackground = Color(0xFFFFFFFF)

val IncomeGreen = Color(0xFF2ECC71)
val ExpenseRed = Color(0xFFE74C3C)

val TextPrimary = Color(0xFF1E1E1E)
val TextSecondary = Color(0xFF6B6B6B)

val PrimaryGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF8EE4AF),
        Color(0xFF2BB673)
    )
)

val BackgroundStart = Color(0xFFE8F5E9)
val BackgroundEnd = Color(0xFFFFFFFF)