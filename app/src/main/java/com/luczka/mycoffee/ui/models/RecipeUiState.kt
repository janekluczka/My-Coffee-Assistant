package com.luczka.mycoffee.ui.models

data class RecipeUiState(
    val imageUrl: String = "",
    val youtubeId: String = "",
    val author: String = "",
    val title: String = "",
    val steps: List<BrewingStepUiState> = emptyList(),
)