package com.coffee.mycoffeeassistant.ui.model

data class RecipeDetailsUiState(
    val author: String = "",
    val title: String = "",
    val youtubeId: String = "",
    val steps: List<Map<String, Any>> = emptyList()
)
