package com.luczka.mycoffee.ui.screens.coffees

import androidx.annotation.StringRes
import com.luczka.mycoffee.R

enum class CoffeeFilterUiState(@StringRes val stringRes: Int) {
    All(R.string.coffee_filter_all),
    Current(R.string.coffee_filter_current),
    Favourites(R.string.coffee_filter_favourites),
    Low(R.string.coffee_filter_low)
}