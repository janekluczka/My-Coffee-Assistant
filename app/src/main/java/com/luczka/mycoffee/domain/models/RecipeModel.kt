package com.luczka.mycoffee.domain.models

data class RecipeModel(
    val youtubeId: String,
    val author: String,
    val title: String,
    val steps: List<StepModel>
)