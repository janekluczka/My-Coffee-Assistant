package com.luczka.mycoffee.ui.mappers

import com.luczka.mycoffee.domain.models.ProcessModel
import com.luczka.mycoffee.ui.models.ProcessUiState

fun ProcessModel.toUiState(): ProcessUiState {
    return when (this) {
        ProcessModel.Natural -> ProcessUiState.Natural
        ProcessModel.Washed -> ProcessUiState.Washed
        ProcessModel.Honey -> ProcessUiState.Honey
        ProcessModel.Other -> ProcessUiState.Other
    }
}

fun ProcessUiState.toModel(): ProcessModel {
    return when (this) {
        ProcessUiState.Natural -> ProcessModel.Natural
        ProcessUiState.Washed -> ProcessModel.Washed
        ProcessUiState.Honey -> ProcessModel.Honey
        ProcessUiState.Other -> ProcessModel.Other
    }
}