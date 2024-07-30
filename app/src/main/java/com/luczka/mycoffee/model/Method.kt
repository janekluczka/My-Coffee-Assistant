package com.luczka.mycoffee.model

import androidx.annotation.Keep
import com.luczka.mycoffee.ui.model.MethodUiState

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