package com.luczka.mycoffee.ui.screens.recipecategories

import com.luczka.mycoffee.ui.models.MethodUiState

sealed class RecipeCategoriesAction {
    data class NavigateToRecipes(val methodUiState: MethodUiState) : RecipeCategoriesAction()
}