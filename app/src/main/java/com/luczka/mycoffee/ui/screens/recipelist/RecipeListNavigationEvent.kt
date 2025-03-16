package com.luczka.mycoffee.ui.screens.recipelist

import com.luczka.mycoffee.ui.models.RecipeUiState

sealed class RecipeListNavigationEvent {
    data object NavigateUp: RecipeListNavigationEvent()
    data class NavigateToRecipeDetails(val recipeUiState: RecipeUiState): RecipeListNavigationEvent()
}