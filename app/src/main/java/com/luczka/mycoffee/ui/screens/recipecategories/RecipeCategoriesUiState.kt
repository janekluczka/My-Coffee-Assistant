package com.luczka.mycoffee.ui.screens.recipecategories

import com.luczka.mycoffee.ui.models.MethodUiState

sealed interface RecipeCategoriesUiState {
    val isLoading: Boolean
    val isError: Boolean
    val methods: List<MethodUiState>

    data class IsError(
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val methods: List<MethodUiState>,
    ) : RecipeCategoriesUiState

    data class NoRecipeCategories(
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val methods: List<MethodUiState>,
        val errorMessage: String,
    ) : RecipeCategoriesUiState

    data class HasRecipeCategories(
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val methods: List<MethodUiState>
    ) : RecipeCategoriesUiState
}