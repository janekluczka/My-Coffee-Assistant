package com.coffee.mycoffeeassistant.models

import com.coffee.mycoffeeassistant.ui.model.MethodUiState

data class Method(
    val name: String = "",
    val imageRef: String = "",
) {
    fun toMethodUiState() : MethodUiState = MethodUiState(
        name = name,
        imageRef = imageRef
    )
}