package com.luczka.mycoffee.data.remote.model

import androidx.annotation.Keep

@Keep
data class CategoryDto(
    val defaultName: String = "",
    val description: String = "",
    val localizedName: Map<String, String> = emptyMap(),
    val id: String = "",
)