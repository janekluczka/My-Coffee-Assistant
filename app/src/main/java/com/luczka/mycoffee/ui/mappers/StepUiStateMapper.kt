package com.luczka.mycoffee.ui.mappers

import com.luczka.mycoffee.domain.models.StepModel
import com.luczka.mycoffee.ui.models.BrewingStepUiState

fun StepModel.toUiState() : BrewingStepUiState {
    return BrewingStepUiState(
        description = description,
        time = time
    )
}