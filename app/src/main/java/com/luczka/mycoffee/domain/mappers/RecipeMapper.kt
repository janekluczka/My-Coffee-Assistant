package com.luczka.mycoffee.domain.mappers

import com.luczka.mycoffee.data.remote.dto.RecipeDto
import com.luczka.mycoffee.domain.models.RecipeModel
import com.luczka.mycoffee.ui.models.RecipeUiState

/**
 * YouTube Video Thumbnail URLs
 *
 * 1280×720 https://i.ytimg.com/vi/[YOUTUBE VIDEO ID]/maxresdefault.jpg
 * 640×480 https://i.ytimg.com/vi/[YOUTUBE VIDEO ID]/sddefault.jpg
 * 480×360 https://i.ytimg.com/vi/[YOUTUBE VIDEO ID]/hqdefault.jpg
 * 320×180 https://i.ytimg.com/vi/[YOUTUBE VIDEO ID]/mqdefault.jpg
 * 120×90 https://i.ytimg.com/vi/[YOUTUBE VIDEO ID]/default.jpg
 */
fun RecipeModel.toUiState() : RecipeUiState {
    return RecipeUiState(
        author = author,
        title = title,
        imageUrl = "https://i.ytimg.com/vi/${youtubeId}/mqdefault.jpg",
        steps = steps.map { it.toUiState() },
        youtubeId = youtubeId
    )
}

fun RecipeDto.toModel() : RecipeModel {
    return RecipeModel(
        author = author,
        title = title,
        youtubeId = youtubeId,
        steps = steps.map { it.toModel() }
    )
}