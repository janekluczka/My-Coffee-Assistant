package com.luczka.mycoffee.data.mappers

import com.luczka.mycoffee.data.remote.dto.CategoryDto
import com.luczka.mycoffee.domain.models.MethodModel

fun CategoryDto.toModel(localeCode: String) : MethodModel {
    return MethodModel(
        id = id,
        name = localizedName[localeCode] ?: defaultName,
        description = description
    )
}

fun List<CategoryDto>.toModel(localeCode: String) : List<MethodModel> {
    return this.map { it.toModel(localeCode) }
}