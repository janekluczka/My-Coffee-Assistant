package com.luczka.mycoffee.domain.models

import androidx.annotation.Keep
import com.luczka.mycoffee.ui.models.MethodUiState

@Keep
data class Method(
    val id: String = "",
    val name: String = ""
) {
    fun toMethodUiState(): MethodUiState = MethodUiState(
        id = id,
        name = name
    )
}