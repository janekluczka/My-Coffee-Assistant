package com.luczka.mycoffee.models

import androidx.annotation.Keep
import com.luczka.mycoffee.ui.components.MethodUiState

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