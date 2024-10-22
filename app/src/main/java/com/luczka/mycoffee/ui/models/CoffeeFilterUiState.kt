package com.luczka.mycoffee.ui.models

import androidx.annotation.StringRes
import com.luczka.mycoffee.R

enum class CoffeeFilterUiState(@StringRes val stringRes: Int) {
    All(R.string.filter_all),
    Current(R.string.filter_current),
    Favourites(R.string.filter_favourites),
    Low(R.string.filter_low)
}