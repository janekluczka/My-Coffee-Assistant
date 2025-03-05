package com.luczka.mycoffee.ui.screens.recipes

import com.luczka.mycoffee.ui.models.RecipeUiState

sealed class RecipesNavigationEvent {
    data object NavigateUp: RecipesNavigationEvent()
    data class NavigateToRecipeDetails(val recipeUiState: RecipeUiState): RecipesNavigationEvent()
}