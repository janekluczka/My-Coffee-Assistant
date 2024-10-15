package com.luczka.mycoffee.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class TopLevelRoute<T : Any>(
    val route: T,
    @StringRes val stringRes: Int,
    @DrawableRes val drawableRes: Int
)