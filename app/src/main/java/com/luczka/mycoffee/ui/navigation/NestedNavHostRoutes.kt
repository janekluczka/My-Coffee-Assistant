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
    data object Methods

    @Serializable
    data class Recipes(val methodUiState: MethodUiState)

    @Serializable
    data class RecipeDetails(val recipeUiState: RecipeUiState)
}