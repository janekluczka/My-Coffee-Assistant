package com.luczka.mycoffee.data.mappers

import com.luczka.mycoffee.data.remote.dto.MethodDto
import com.luczka.mycoffee.domain.models.MethodModel

fun MethodDto.toModel(localeCode: String) : MethodModel {
    return MethodModel(
        id = id,
        name = localizedName[localeCode] ?: defaultName,
        description = description
    )
}