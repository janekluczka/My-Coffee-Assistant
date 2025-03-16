package com.luczka.mycoffee.ui.screens.brews

import androidx.annotation.StringRes
import com.luczka.mycoffee.R

enum class BrewFilterUiState(@StringRes val stringRes: Int) {
    LATEST(R.string.brew_filter_latest),
    BEST_RATED(R.string.brew_filter_best_rated)
}