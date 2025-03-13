package com.luczka.mycoffee.ui.screens.recipedetails

import com.luczka.mycoffee.ui.models.RecipeUiState

data class RecipeDetailsUiState(
    val recipe: RecipeUiState,
    val openLeaveApplicationDialog: Boolean,
)