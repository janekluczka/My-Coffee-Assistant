package com.luczka.mycoffee.ui.screens.recipelist

import com.luczka.mycoffee.ui.models.RecipeUiState

sealed class RecipeListAction {
    data object NavigateUp : RecipeListAction()
    data class NavigateToRecipeDetails(val recipeUiState: RecipeUiState) : RecipeListAction()
    data object ShowMethodInfoDialog : RecipeListAction()
    data object HideMethodInfoDialog : RecipeListAction()
}