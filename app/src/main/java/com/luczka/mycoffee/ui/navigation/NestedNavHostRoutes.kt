package com.luczka.mycoffee.ui.navigation

import com.luczka.mycoffee.ui.models.MethodUiState
import com.luczka.mycoffee.ui.models.RecipeUiState
import kotlinx.serialization.Serializable

data object NestedNavHostRoutes {
    @Serializable
    data object Home

    @Serializable
    data object Brews

    @Serializable
    data object Coffees

    @Serializable
    data object Equipment

    @Serializable
    sealed class Recipes {
        @Serializable
        data object Categories : Recipes()

        @Serializable
        data class List (val methodUiState: MethodUiState) : Recipes()

        @Serializable
        data class Details(val recipeUiState: RecipeUiState) : Recipes()
    }
}