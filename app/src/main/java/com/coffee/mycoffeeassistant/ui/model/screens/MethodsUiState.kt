package com.coffee.mycoffeeassistant.ui.model.screens

import com.coffee.mycoffeeassistant.ui.model.components.MethodCardUiState

data class MethodsUiState(
    val methodsList: List<MethodCardUiState> = emptyList()
)
