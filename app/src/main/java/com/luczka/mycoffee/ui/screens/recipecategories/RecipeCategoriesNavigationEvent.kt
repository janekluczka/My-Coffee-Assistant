package com.luczka.mycoffee.ui.screens.recipecategories

import com.luczka.mycoffee.ui.models.MethodUiState

sealed class RecipeCategoriesNavigationEvent {
    data class NavigateToMethodDetails(val methodUiState: MethodUiState) : RecipeCategoriesNavigationEvent()
}