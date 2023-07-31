package com.coffee.mycoffeeassistant.ui.model.components

data class RecipeDetailsUiState(
    val youtubeId: String = "",
    val iframeHtml: String = "",
    val author: String = "",
    val title: String = "",
    val steps: List<StepUiState> = emptyList(),
)

data class StepUiState(
    val number: Int = 0,
    val description: String = "",
    val time: String? = null,
)