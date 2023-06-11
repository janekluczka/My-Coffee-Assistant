package com.coffee.mycoffeeassistant.ui.model

data class BrewAssistantUiState(
    val isCoffeeSelected: Boolean = false,
    val isCoffeeAmountWrong: Boolean = false,
    val isWaterAmountWrong: Boolean = false,
    val selectedCoffee: CoffeeUiState? = null
)