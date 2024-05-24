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
        painterResource = R.drawable.ic_baseline_home,
        route = MyCoffeeDestinations.ROUTE_HOME
    ),
    HISTORY(
        stringResource = R.string.tab_history,
        painterResource = R.drawable.ic_baseline_history,
        route = MyCoffeeDestinations.ROUTE_HISTORY
    ),
    COFFEES(
        stringResource = R.string.tab_coffees,
        painterResource = R.drawable.ic_baseline_auto_awesome_motion,
        route = MyCoffeeDestinations.ROUTE_COFFEES
    ),
    RECIPES(
        stringResource = R.string.tab_recipes,
        painterResource = R.drawable.ic_baseline_format_list_bulleted,
        route = MyCoffeeDestinations.ROUTE_RECIPES
    ),
}