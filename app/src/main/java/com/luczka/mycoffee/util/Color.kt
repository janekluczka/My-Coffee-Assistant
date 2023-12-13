package com.luczka.mycoffee.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.ln

fun Color.colorAtElevation(
    elevation: Dp,
    surface: Color
): Color {
    if (elevation == 0.dp) return this
    val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
    return this.copy(alpha = alpha).compositeOver(surface)
}