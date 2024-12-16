package com.luczka.mycoffee.ui.screens.methods

import com.luczka.mycoffee.ui.models.MethodUiState

sealed class MethodsAction {
    data class NavigateToRecipes(val methodUiState: MethodUiState) : MethodsAction()
}