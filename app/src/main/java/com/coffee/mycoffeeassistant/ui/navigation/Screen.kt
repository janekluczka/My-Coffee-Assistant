@file:OptIn(ExperimentalMaterial3Api::class)

package com.coffee.mycoffeeassistant.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import com.coffee.mycoffeeassistant.R

sealed class Screen(
    val route: String,
    @StringRes val stringResource: Int = R.string.unknown,
    @DrawableRes val painterResource: Int = R.drawable.ic_launcher_foreground,
    val fabPosition: FabPosition = FabPosition.Center
) {
    object Home : Screen(
        route = "home_screen",
        stringResource = R.string.home,
        painterResource = R.drawable.ic_baseline_home
    )

    object BrewAssistant : Screen(
        route = "home_screen/brew_assistant"
    )

    object Cupboard : Screen(
        route = "cupboard_screen",
        stringResource = R.string.cupboard,
        painterResource = R.drawable.ic_baseline_view_carousel,
        fabPosition = FabPosition.End
    )

    object AddCoffee : Screen(
        route = "cupboard_screen/add_coffee"
    )

    object CoffeeDetails : Screen(
        route = "cupboard_screen/coffee_details"
    )

    object Recipes : Screen(
        route = "recipes_screen",
        stringResource = R.string.recipes,
        painterResource = R.drawable.ic_baseline_format_list_bulleted
    )

    object MethodRecipes : Screen(
        route = "recipes_screen/method"
    )

    object RecipeDetails : Screen(
        route = "recipes_screen/method/details"
    )
}