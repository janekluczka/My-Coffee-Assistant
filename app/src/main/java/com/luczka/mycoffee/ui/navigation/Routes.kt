package com.luczka.mycoffee.ui.navigation

import com.luczka.mycoffee.R
import kotlinx.serialization.Serializable

@Serializable
data object Routes {

    @Serializable
    data object Main {
        val topLevelRoutes = listOf(
            TopLevelRoute(
                route = Home,
                stringRes = R.string.tab_home,
                drawableRes = R.drawable.home_24px
            ),
            TopLevelRoute(
                route = History,
                stringRes = R.string.tab_history,
                drawableRes = R.drawable.history_24px
            ),
            TopLevelRoute(
                route = Coffees,
                stringRes = R.string.tab_coffees,
                drawableRes = R.drawable.browse_24px
            ),
            TopLevelRoute(
                route = Methods,
                stringRes = R.string.tab_recipes,
                drawableRes = R.drawable.format_list_bulleted_24px
            )
        )

        @Serializable
        data object Home

        @Serializable
        data object History

        @Serializable
        data class HistoryDetails(val brewId: Int)

        @Serializable
        data object Coffees

        @Serializable
        data class CoffeeDetails(val coffeeId: Int)

        @Serializable
        data object Methods

        @Serializable
        data class Recipes(val methodId: String)

        @Serializable
        data class RecipeDetails(val recipeId: String)
    }

    @Serializable
    data object Assistant

    @Serializable
    data class CoffeeInput(val coffeeId: Int? = null)

}