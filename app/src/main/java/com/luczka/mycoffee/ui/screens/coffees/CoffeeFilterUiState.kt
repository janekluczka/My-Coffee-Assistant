package com.luczka.mycoffee.ui.screens.coffees

import androidx.annotation.StringRes
import com.luczka.mycoffee.R

enum class CoffeeFilterUiState(@StringRes val stringRes: Int) {
    All(R.string.coffee_filter_all),
    Favourites(R.string.coffee_filter_favourites),
}