package com.luczka.mycoffee.ui.screens.recipelist

import com.luczka.mycoffee.ui.models.MethodUiState
import com.luczka.mycoffee.ui.models.RecipeUiState

sealed interface RecipeListUiState {
    val methodUiState: MethodUiState
    val hasInfoButton: Boolean
    val isLoading: Boolean
    val isError: Boolean
    val recipes: List<RecipeUiState>
    val openMethodInfoDialog: Boolean

    data class NoRecipes(
        override val methodUiState: MethodUiState,
        override val hasInfoButton: Boolean,
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val recipes: List<RecipeUiState> = emptyList(),
        override val openMethodInfoDialog: Boolean,
        val errorMessage: String
    ) : RecipeListUiState

    data class HasRecipes(
        override val methodUiState: MethodUiState,
        override val hasInfoButton: Boolean,
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val recipes: List<RecipeUiState>,
        override val openMethodInfoDialog: Boolean
    ) : RecipeListUiState
}