package com.luczka.mycoffee.data.mapper

import com.luczka.mycoffee.data.remote.model.CategoryDto
import com.luczka.mycoffee.domain.models.CategoryModel

fun CategoryDto.toModel(localeCode: String) : CategoryModel {
    return CategoryModel(
        id = id,
        name = localizedName[localeCode] ?: defaultName,
        description = description
    )
}

fun List<CategoryDto>.toModel(localeCode: String) : List<CategoryModel> {
    return this.map { it.toModel(localeCode) }
}