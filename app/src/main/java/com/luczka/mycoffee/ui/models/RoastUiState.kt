package com.luczka.mycoffee.ui.models

import androidx.annotation.StringRes
import com.luczka.mycoffee.R

/**
 * Represents the different coffee roast levels in the UI layer.
 *
 * @property stringRes The string resource ID for the roast name.
 */
enum class RoastUiState(@StringRes val stringRes: Int) {
    Light(R.string.roast_light),
    Medium(R.string.roast_medium),
    MediumDark(R.string.roast_medium_dark),
    Dark(R.string.roast_dark)
}