package com.luczka.mycoffee.data.remote.model

import androidx.annotation.Keep

@Keep
data class RecipeDto(
    val author: String = "",
    val title: String = "",
    val steps: List<StepDto> = emptyList(),
    val youtubeId: String = "",
)