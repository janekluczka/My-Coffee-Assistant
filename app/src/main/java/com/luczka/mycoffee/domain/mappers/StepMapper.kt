package com.luczka.mycoffee.domain.mappers

import com.luczka.mycoffee.data.remote.dto.StepDto
import com.luczka.mycoffee.domain.models.StepModel
import com.luczka.mycoffee.ui.models.StepUiState

fun StepModel.toUiState() : StepUiState {
    return StepUiState(
        description = description,
        time = time
    )
}

fun StepDto.toModel() : StepModel {
    return StepModel(
        description = description,
        time = time
    )
}