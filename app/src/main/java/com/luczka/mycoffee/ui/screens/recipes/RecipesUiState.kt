package com.luczka.mycoffee.ui.screens.recipes

import com.luczka.mycoffee.ui.models.MethodUiState
import com.luczka.mycoffee.ui.models.RecipeUiState

sealed interface RecipesUiState {
    val methodUiState: MethodUiState
    val showInfoButton: Boolean
    val isLoading: Boolean
    val isError: Boolean
    val recipes: List<RecipeUiState>

    data class NoRecipes(
        override val methodUiState: MethodUiState,
        override val showInfoButton: Boolean,
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val recipes: List<RecipeUiState> = emptyList(),
        val errorMessage: String,
    ) : RecipesUiState

    data class HasRecipes(
        override val methodUiState: MethodUiState,
        override val showInfoButton: Boolean,
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val recipes: List<RecipeUiState>,
    ) : RecipesUiState
}