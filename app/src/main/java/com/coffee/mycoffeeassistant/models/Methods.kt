package com.coffee.mycoffeeassistant.models

import com.coffee.mycoffeeassistant.ui.model.components.MethodCardUiState
import com.coffee.mycoffeeassistant.ui.model.components.RecipeCardUiState

data class Methods(
    val methods: List<Method> = emptyList()
)

data class Method(
    val id: String = "",
    val name: String = "",
    val imageRef: String = "",
    var imageUrl: String = ""
) {
    fun toUrlImageCardUiState(): RecipeCardUiState = RecipeCardUiState(
        id = id,
        title = name,
        url = imageUrl,
    )

    fun toMethodCardUiState(): MethodCardUiState = MethodCardUiState(
        id = id,
        title = name,
        url = imageUrl,
    )

}