package com.luczka.mycoffee.enums

import com.luczka.mycoffee.R

enum class CoffeeFilter(val id: String, val stringResource: Int) {
    Current("current", R.string.filter_current),
    All("all", R.string.filter_all),
    Favourites("favourites", R.string.filter_favourites),
    Low("low", R.string.filter_low),
    Older("older", R.string.filter_older),
}