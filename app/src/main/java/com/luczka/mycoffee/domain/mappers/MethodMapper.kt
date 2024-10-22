package com.luczka.mycoffee.domain.mappers

import com.luczka.mycoffee.data.remote.dto.MethodDto
import com.luczka.mycoffee.domain.models.MethodModel
import com.luczka.mycoffee.ui.models.MethodUiState

fun MethodModel.toUiState() : MethodUiState {
    return MethodUiState(
        id = id,
        name = name
    )
}

fun MethodDto.toModel() : MethodModel {
    return MethodModel(
        id = id,
        name = name
    )
}