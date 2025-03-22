package com.luczka.mycoffee.ui.screens.recipelist

import com.luczka.mycoffee.ui.models.CategoryUiState
import com.luczka.mycoffee.ui.models.RecipeUiState

sealed interface RecipeListUiState {
    val categoryUiState: CategoryUiState
    val hasInfoButton: Boolean
    val isLoading: Boolean
    val isError: Boolean
    val recipes: List<RecipeUiState>
    val openMethodInfoDialog: Boolean

    data class NoRecipes(
        override val categoryUiState: CategoryUiState,
        override val hasInfoButton: Boolean,
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val recipes: List<RecipeUiState> = emptyList(),
        override val openMethodInfoDialog: Boolean,
        val errorMessage: String
    ) : RecipeListUiState

    data class HasRecipes(
        override val categoryUiState: CategoryUiState,
        override val hasInfoButton: Boolean,
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val recipes: List<RecipeUiState>,
        override val openMethodInfoDialog: Boolean
    ) : RecipeListUiState
}