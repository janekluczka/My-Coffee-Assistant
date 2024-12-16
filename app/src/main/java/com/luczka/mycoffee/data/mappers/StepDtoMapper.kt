package com.luczka.mycoffee.data.mappers

import com.luczka.mycoffee.data.remote.dto.StepDto
import com.luczka.mycoffee.domain.models.StepModel

fun StepDto.toModel() : StepModel {
    return StepModel(
        description = description,
        time = time
    )
}