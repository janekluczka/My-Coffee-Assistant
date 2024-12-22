package com.luczka.mycoffee.ui.screens.brews

import androidx.annotation.StringRes
import com.luczka.mycoffee.R

enum class BrewFilterUiState(@StringRes val stringRes: Int) {
    LATEST(R.string.filter_latest),
    BEST_RATED(R.string.filter_best_rated)
}