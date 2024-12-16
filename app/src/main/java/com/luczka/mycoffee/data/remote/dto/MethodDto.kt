package com.luczka.mycoffee.data.remote.dto

import androidx.annotation.Keep

@Keep
data class MethodDto(
    val defaultName: String = "",
    val description: String = "",
    val localizedName: Map<String, String> = emptyMap(),
    val id: String = "",
)