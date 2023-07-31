package com.coffee.mycoffeeassistant.ui.model.screens

import com.coffee.mycoffeeassistant.ui.model.components.RecipeCardUiState

data class MethodRecipesUiState(
    val title: String = "",
    val recipesList: List<RecipeCardUiState> = emptyList(),
)
