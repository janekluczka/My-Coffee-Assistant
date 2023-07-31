package com.coffee.mycoffeeassistant.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.coffee.mycoffeeassistant.R

sealed class Screen(
    val route: String,
    @StringRes val stringResource: Int = R.string.unknown,
    @DrawableRes val painterResource: Int = R.drawable.ic_launcher_foreground,
) {
    fun createRoute(id: Any) = route + "/${id}"

    object Home : Screen(
        route = "home_screen",
        stringResource = R.string.tab_home,
        painterResource = R.drawable.ic_baseline_home
    )

    object BrewAssistant : Screen(
        route = "home_screen/brew_assistant"
    )

    object Cupboard : Screen(
        route = "cupboard_screen",
        stringResource = R.string.tab_cupboard,
        painterResource = R.drawable.ic_baseline_view_carousel
    )

    object AddCoffee : Screen(
        route = "cupboard_screen/add_coffee",
    )

    object HomeCoffeeDetails : Screen(
        route = "home_screen/coffee_details"
    )

    object CupboardCoffeeDetails : Screen(
        route = "cupboard_screen/coffee_details"
    )

    object Recipes : Screen(
        route = "recipes_screen",
        stringResource = R.string.tab_recipes,
        painterResource = R.drawable.ic_baseline_format_list_bulleted
    )

    object MethodRecipes : Screen(
        route = "recipes_screen/method"
    )

    object RecipeDetails : Screen(
        route = "recipes_screen/method/details"
    )
}