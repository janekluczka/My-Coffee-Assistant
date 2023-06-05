package com.coffee.mycoffeeassistant.models

import com.coffee.mycoffeeassistant.ui.model.RecipeDetailsUiState
import com.coffee.mycoffeeassistant.ui.model.RecipeUiState

data class Recipe(
    val author: String = "",
    val title: String = "",
    val youtubeId: String = "",
    val steps: List<Map<String, Any>> = emptyList()
) {
    fun toRecipeUiState(): RecipeUiState = RecipeUiState(
        author = author,
        title = title,
        youtubeId = youtubeId
    )

    fun toRecipeDetailsUiState(): RecipeDetailsUiState = RecipeDetailsUiState(
        author = author,
        title = title,
        youtubeId = youtubeId,
        steps = steps
    )
}
