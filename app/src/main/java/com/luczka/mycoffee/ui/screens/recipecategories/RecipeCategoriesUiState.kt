package com.luczka.mycoffee.ui.screens.recipecategories

import com.luczka.mycoffee.ui.models.CategoryUiState

sealed interface RecipeCategoriesUiState {
    val isLoading: Boolean
    val isError: Boolean
    val errorMessageRes: Int

    data class NoRecipeCategories(
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val errorMessageRes: Int,
    ) : RecipeCategoriesUiState

    data class HasRecipeCategories(
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val errorMessageRes: Int,
        val categories: List<CategoryUiState>
    ) : RecipeCategoriesUiState
}