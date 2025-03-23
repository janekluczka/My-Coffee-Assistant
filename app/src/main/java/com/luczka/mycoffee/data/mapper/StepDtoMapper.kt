package com.luczka.mycoffee.data.mapper

import com.luczka.mycoffee.data.remote.model.StepDto
import com.luczka.mycoffee.domain.models.StepModel

fun StepDto.toModel() : StepModel {
    return StepModel(
        description = description,
        time = time
    )
}

fun List<StepDto>.toModel() : List<StepModel> {
    return this.map { it.toModel() }
}