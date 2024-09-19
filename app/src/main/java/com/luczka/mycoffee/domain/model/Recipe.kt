package com.luczka.mycoffee.domain.model

import androidx.annotation.Keep
import com.luczka.mycoffee.ui.model.RecipeUiState

/**
 * YouTube Video Thumbnail URLs
 *
 * 1280×720 https://i.ytimg.com/vi/[YOUTUBE VIDEO ID]/maxresdefault.jpg
 * 640×480 https://i.ytimg.com/vi/[YOUTUBE VIDEO ID]/sddefault.jpg
 * 480×360 https://i.ytimg.com/vi/[YOUTUBE VIDEO ID]/hqdefault.jpg
 * 320×180 https://i.ytimg.com/vi/[YOUTUBE VIDEO ID]/mqdefault.jpg
 * 120×90 https://i.ytimg.com/vi/[YOUTUBE VIDEO ID]/default.jpg
 */
@Keep
data class Recipe(
    val author: String = "",
    val title: String = "",
    val youtubeId: String = "",
    val steps: List<Step> = emptyList()
) {

    fun toRecipeDetailsUiState(): RecipeUiState = RecipeUiState(
        imageUrl = "https://i.ytimg.com/vi/${youtubeId}/mqdefault.jpg",
        author = author,
        title = title,
        youtubeId = youtubeId,
        steps = steps.mapIndexed { index, step -> step.toStepUiState(index = index) }
    )
}

