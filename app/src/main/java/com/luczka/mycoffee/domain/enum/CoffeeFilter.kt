package com.luczka.mycoffee.domain.enum

import com.luczka.mycoffee.R

enum class CoffeeFilter(val id: String, val stringResource: Int) {
    All("all", R.string.filter_all),
    Current("current", R.string.filter_current),
    Favourites("favourites", R.string.filter_favourites),
    Low("low", R.string.filter_low),
    Older("older", R.string.filter_older),
}