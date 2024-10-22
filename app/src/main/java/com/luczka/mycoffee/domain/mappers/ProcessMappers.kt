package com.luczka.mycoffee.domain.mappers

import com.luczka.mycoffee.data.database.types.ProcessType
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

fun ProcessType.toModel(): ProcessModel {
    return when (this) {
        ProcessType.Natural -> ProcessModel.Natural
        ProcessType.Washed -> ProcessModel.Washed
        ProcessType.Honey -> ProcessModel.Honey
        ProcessType.Other -> ProcessModel.Other
    }
}

fun ProcessModel.toType(): ProcessType {
    return when (this) {
        ProcessModel.Natural -> ProcessType.Natural
        ProcessModel.Washed -> ProcessType.Washed
        ProcessModel.Honey -> ProcessType.Honey
        ProcessModel.Other -> ProcessType.Other
    }
}