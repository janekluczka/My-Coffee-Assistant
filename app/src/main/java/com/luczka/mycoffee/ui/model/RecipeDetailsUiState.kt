package com.luczka.mycoffee.ui.model

data class RecipeDetailsUiState(
    val imageUrl: String = "",
    val youtubeId: String = "",
    val author: String = "",
    val title: String = "",
    val steps: List<StepUiState> = emptyList(),
)