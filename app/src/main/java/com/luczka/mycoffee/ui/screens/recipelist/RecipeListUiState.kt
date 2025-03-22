package com.luczka.mycoffee.ui.screens.recipelist

import com.luczka.mycoffee.ui.models.CategoryUiState
import com.luczka.mycoffee.ui.models.RecipeUiState

sealed interface RecipeListUiState {
    val categoryUiState: CategoryUiState
    val hasInfoButton: Boolean
    val openMethodInfoDialog: Boolean
    val isLoading: Boolean
    val isError: Boolean
    val errorMessageRes: Int

    data class NoRecipes(
        override val categoryUiState: CategoryUiState,
        override val hasInfoButton: Boolean,
        override val openMethodInfoDialog: Boolean,
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val errorMessageRes: Int,
    ) : RecipeListUiState

    data class HasRecipes(
        override val categoryUiState: CategoryUiState,
        override val hasInfoButton: Boolean,
        override val openMethodInfoDialog: Boolean,
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val errorMessageRes: Int,
        val recipes: List<RecipeUiState>,
    ) : RecipeListUiState
}