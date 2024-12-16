package com.luczka.mycoffee.data.mappers

import com.luczka.mycoffee.data.remote.dto.RecipeDto
import com.luczka.mycoffee.domain.models.RecipeModel

fun RecipeDto.toModel() : RecipeModel {
    return RecipeModel(
        author = author,
        title = title,
        youtubeId = youtubeId,
        steps = steps.map { it.toModel() }
    )
}