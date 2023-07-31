package com.coffee.mycoffeeassistant.models

import com.coffee.mycoffeeassistant.ui.model.components.RecipeDetailsUiState
import com.coffee.mycoffeeassistant.ui.model.components.StepUiState
import com.coffee.mycoffeeassistant.ui.model.components.RecipeCardUiState

/**
 * YouTube Video Thumbnail URLs
 *
 * 1280×720 https://i.ytimg.com/vi/[YOUTUBE VIDEO ID]/maxresdefault.jpg
 * 640×480 https://i.ytimg.com/vi/[YOUTUBE VIDEO ID]/sddefault.jpg
 * 480×360 https://i.ytimg.com/vi/[YOUTUBE VIDEO ID]/hqdefault.jpg
 * 320×180 https://i.ytimg.com/vi/[YOUTUBE VIDEO ID]/mqdefault.jpg
 * 120×90 https://i.ytimg.com/vi/[YOUTUBE VIDEO ID]/default.jpg
 */
data class Recipe(
    val author: String = "",
    val title: String = "",
    val youtubeId: String = "",
    val steps: List<Step> = emptyList()
) {
    fun toRecipeCardUiState(): RecipeCardUiState = RecipeCardUiState(
        id = youtubeId,
        title = title,
        body = author.ifBlank { null },
        url = "https://i.ytimg.com/vi/${youtubeId}/sddefault.jpg",
    )

    fun toRecipeDetailsUiState(): RecipeDetailsUiState = RecipeDetailsUiState(
        author = author,
        title = title,
        youtubeId = youtubeId,
        steps = steps.mapIndexed { index, step -> step.toStepUiState(index = index) }
    )
}

data class Step(
    val description: String = "",
    val time: String? = null,
) {
    fun toStepUiState(index: Int): StepUiState = StepUiState(
        number = index + 1,
        description = description,
        time = time
    )
}
