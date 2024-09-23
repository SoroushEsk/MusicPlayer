package com.soroush.musicapp.page.music.ui.theme

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path

val customShape = GenericShape { size: Size, _ ->
    val path = Path().apply {
        // Draw rectangle
        addRect(Rect(0f, 0f, size.width, size.height))
        // Draw circle
        val circleRadius = size.width / 2
        val circleCenter = size.width / 2
        val circleTop = size.height / 2
        addOval(
            Rect(
                left = circleCenter - circleRadius,
                top = circleTop - circleRadius,
                right = circleCenter + circleRadius,
                bottom = circleTop + circleRadius
            )
        )
    }
    addPath(path)
}
