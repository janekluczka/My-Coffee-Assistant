package com.luczka.mycoffee.data.remote.dto

import androidx.annotation.Keep

@Keep
data class CategoriesDto(
    val list: List<CategoryDto> = emptyList()
)
