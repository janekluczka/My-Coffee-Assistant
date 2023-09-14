package com.luczka.mycoffee.models

import androidx.annotation.Keep
import com.luczka.mycoffee.ui.screens.methods.MethodUiState

@Keep
data class Methods(
    val methods: List<Method> = emptyList()
)

@Keep
data class Method(
    val id: String = "",
    val name: String = "",
    val imageRef: String = "",
    var imageUrl: String = ""
) {
    fun toMethodCardUiState(): MethodUiState = MethodUiState(
        id = id,
        title = name,
        url = imageUrl,
    )
}