package com.luczka.mycoffee.data.remote.dto

import androidx.annotation.Keep

@Keep
data class MethodsDto(
    val list: List<MethodDto> = emptyList()
)
