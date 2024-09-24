package com.luczka.mycoffee.ui.screens.recipes

sealed class RecipesAction {
    object NavigateUp : RecipesAction()
    data class NavigateToRecipeDetails(val methodId: String, val recipeId: String) : RecipesAction()
}