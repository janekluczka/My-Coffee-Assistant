package com.luczka.mycoffee.ui.screens.recipecategories

import com.luczka.mycoffee.ui.models.CategoryUiState

sealed class RecipeCategoriesAction {
    data class NavigateToRecipes(val categoryUiState: CategoryUiState) : RecipeCategoriesAction()
}