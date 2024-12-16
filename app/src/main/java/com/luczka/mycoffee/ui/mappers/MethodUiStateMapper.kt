package com.luczka.mycoffee.ui.mappers

import com.luczka.mycoffee.domain.models.MethodModel
import com.luczka.mycoffee.ui.models.MethodUiState

fun MethodModel.toUiState() : MethodUiState {
    return MethodUiState(
        id = id,
        name = name,
        description = description
    )
}