package com.luczka.mycoffee.data.mappers

import com.luczka.mycoffee.data.database.types.ProcessType
import com.luczka.mycoffee.domain.models.ProcessModel

fun ProcessModel.toType(): ProcessType {
    return when (this) {
        ProcessModel.Natural -> ProcessType.Natural
        ProcessModel.Washed -> ProcessType.Washed
        ProcessModel.Honey -> ProcessType.Honey
        ProcessModel.Other -> ProcessType.Other
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