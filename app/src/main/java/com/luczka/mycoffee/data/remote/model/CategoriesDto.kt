package com.luczka.mycoffee.data.remote.model

import androidx.annotation.Keep

@Keep
data class CategoriesDto(
    val list: List<CategoryDto> = emptyList()
)
