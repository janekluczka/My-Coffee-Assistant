package com.luczka.mycoffee.ui.screens.recipecategories

import com.luczka.mycoffee.ui.models.CategoryUiState

sealed class RecipeCategoriesOneTimeEvent {
    data class NavigateToMethodDetails(val categoryUiState: CategoryUiState) : RecipeCategoriesOneTimeEvent()
}