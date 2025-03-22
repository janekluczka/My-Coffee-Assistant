package com.luczka.mycoffee.data.remote.model

import androidx.annotation.Keep

@Keep
data class StepDto(
    val description: String = "",
    val time: String? = null,
)