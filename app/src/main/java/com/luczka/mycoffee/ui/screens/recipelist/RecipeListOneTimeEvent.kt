package com.luczka.mycoffee.ui.screens.recipelist

import com.luczka.mycoffee.ui.models.RecipeUiState

sealed class RecipeListOneTimeEvent {
    data object NavigateUp: RecipeListOneTimeEvent()
    data class NavigateToRecipeDetails(val recipeUiState: RecipeUiState): RecipeListOneTimeEvent()
}