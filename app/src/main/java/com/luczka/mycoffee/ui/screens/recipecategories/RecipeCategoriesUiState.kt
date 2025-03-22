package com.luczka.mycoffee.ui.screens.recipecategories

import com.luczka.mycoffee.ui.models.CategoryUiState

sealed interface RecipeCategoriesUiState {
    val isLoading: Boolean
    val isError: Boolean
    val methods: List<CategoryUiState>

    data class IsError(
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val methods: List<CategoryUiState>,
    ) : RecipeCategoriesUiState

    data class NoRecipeCategories(
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val methods: List<CategoryUiState>,
        val errorMessage: String,
    ) : RecipeCategoriesUiState

    data class HasRecipeCategories(
        override val isLoading: Boolean,
        override val isError: Boolean,
        override val methods: List<CategoryUiState>
    ) : RecipeCategoriesUiState
}