package com.soroush.eskandarie.musicplayer.presentation.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

fun Color.getReadableTextColor(isSystemDark: Boolean): Color {
    val isDark = (this.luminance() < 0.5f).xor(isSystemDark)

    return if (isDark.not()) lightenColor(this, 0.6f) else darkenColor(this, 0.6f)
}
private fun lightenColor(color: Color, amount: Float): Color {
    return Color(
        red = color.red + (1f - color.red) * amount,
        green = color.green + (1f - color.green) * amount,
        blue = color.blue + (1f - color.blue) * amount,
        alpha = color.alpha
    )
}

private fun darkenColor(color: Color, amount: Float): Color {
    return Color(
        red = color.red * (1f - amount),
        green = color.green * (1f - amount),
        blue = color.blue * (1f - amount),
        alpha = color.alpha
    )
}