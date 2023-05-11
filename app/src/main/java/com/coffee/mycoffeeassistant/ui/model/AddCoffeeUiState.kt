package com.coffee.mycoffeeassistant.ui.model

data class AddCoffeeUiState(
    val isNameWrong: Boolean = false,
    val isBrandWrong: Boolean = false,
    val isAmountWrong: Boolean = false
)