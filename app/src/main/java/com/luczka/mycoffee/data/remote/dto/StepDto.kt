package com.luczka.mycoffee.data.remote.dto

import androidx.annotation.Keep

@Keep
data class StepDto(
    val description: String = "",
    val time: String? = null,
)