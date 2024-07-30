package com.luczka.mycoffee.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.luczka.mycoffee.R

enum class HomeTabs(
    val route: String,
    @StringRes val stringResource: Int,
    @DrawableRes val painterResource: Int
) {
    HOME(
        stringResource = R.string.tab_home,
        painterResource = R.drawable.home_24px,
        route = MyCoffeeDestinations.ROUTE_HOME
    ),
    HISTORY(
        stringResource = R.string.tab_history,
        painterResource = R.drawable.history_24px,
        route = MyCoffeeDestinations.ROUTE_HISTORY
    ),
    COFFEES(
        stringResource = R.string.tab_coffees,
        painterResource = R.drawable.browse_24px,
        route = MyCoffeeDestinations.ROUTE_COFFEES
    ),
    RECIPES(
        stringResource = R.string.tab_recipes,
        painterResource = R.drawable.format_list_bulleted_24px,
        route = MyCoffeeDestinations.ROUTE_RECIPES
    ),
}