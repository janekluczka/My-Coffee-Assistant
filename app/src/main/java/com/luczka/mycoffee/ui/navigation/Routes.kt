package com.luczka.mycoffee.ui.navigation

import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.models.MethodUiState
import kotlinx.serialization.Serializable

@Serializable
data object Routes {

    @Serializable
    data object Main {
        val topLevelRoutes = listOf(
            TopLevelRoute(
                route = Home,
                stringRes = R.string.tab_home,
                drawableRes = R.drawable.ic_home_24_fill_0_weight_300_grade_0_opticalsize_24
            ),
            TopLevelRoute(
                route = History,
                stringRes = R.string.tab_history,
                drawableRes = R.drawable.ic_history_24_fill_0_weight_300_grade_0_opticalsize_24
            ),
            TopLevelRoute(
                route = Coffees,
                stringRes = R.string.tab_coffees,
                drawableRes = R.drawable.ic_browse_24_fill_0_weight_300_grade_0_opticalsize_24
            ),
//            TopLevelRoute(
//                route = Equipment,
//                stringRes = R.string.tab_equipment,
//                drawableRes = R.drawable.ic_coffee_maker_24_fill_0_weight_300_grade_0_opticalsize_24
//            ),
            TopLevelRoute(
                route = Methods,
                stringRes = R.string.tab_recipes,
                drawableRes = R.drawable.ic_list_alt_24_fill_0_weight_300_grade_0_opticalsize_24
            )
        )

        @Serializable
        data object Home

        @Serializable
        data object History

        @Serializable
        data object Coffees

        @Serializable
        data object Equipment

        @Serializable
        data object Methods

        @Serializable
        data class Recipes(val methodUiState: MethodUiState)

        @Serializable
        data class RecipeDetails(val recipeId: String)
    }

    @Serializable
    data class BrewDetails(val brewId: Long)

    @Serializable
    data object Assistant

    @Serializable
    data class AssistantRating(val brewId: Long)

    @Serializable
    data class CoffeeDetails(val coffeeId: Long)

    @Serializable
    data class CoffeeInput(val coffeeId: Long? = null)

    @Serializable
    data class EquipmentInput(val equipmentId: Long? = null)

}