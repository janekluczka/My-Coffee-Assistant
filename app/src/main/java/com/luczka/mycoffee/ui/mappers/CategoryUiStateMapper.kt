package com.luczka.mycoffee.ui.mappers

import com.luczka.mycoffee.domain.models.CategoryModel
import com.luczka.mycoffee.ui.models.CategoryUiState

fun CategoryModel.toUiState() : CategoryUiState {
    return CategoryUiState(
        id = id,
        name = name,
        description = description
    )
}

fun List<CategoryModel>.toUiState() : List<CategoryUiState> {
    return map { it.toUiState() }
}