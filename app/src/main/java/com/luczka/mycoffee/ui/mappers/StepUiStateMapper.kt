package com.luczka.mycoffee.ui.mappers

import com.luczka.mycoffee.domain.models.StepModel
import com.luczka.mycoffee.ui.models.BrewingStepUiState

fun StepModel.toUiState(number: Int): BrewingStepUiState {
    return BrewingStepUiState(
        number = number,
        description = description,
        time = time
    )
}

fun List<StepModel>.toUiState(): List<BrewingStepUiState> {
    return this.mapIndexed { index, stepModel ->
        stepModel.toUiState(number = index + 1)
    }
}