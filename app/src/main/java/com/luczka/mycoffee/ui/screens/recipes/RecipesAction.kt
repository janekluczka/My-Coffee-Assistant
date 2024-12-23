package com.luczka.mycoffee.ui.screens.recipes

import com.luczka.mycoffee.ui.models.RecipeUiState

sealed class RecipesAction {
    object NavigateUp : RecipesAction()
    data class NavigateToRecipeDetails(val recipeUiState: RecipeUiState) : RecipesAction()
}