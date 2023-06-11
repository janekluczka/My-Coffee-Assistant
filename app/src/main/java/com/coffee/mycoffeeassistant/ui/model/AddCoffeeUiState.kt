package com.coffee.mycoffeeassistant.ui.model

data class AddCoffeeUiState(
    val isNameWrong: Boolean = false,
    val isBrandWrong: Boolean = false,
    val isAmountWrong: Boolean = false,
) {
    fun markWrongFields(name: String, brand: String, currentAmount: String): AddCoffeeUiState =
        AddCoffeeUiState(
            isNameWrong = name.isEmpty() || name.isBlank(),
            isBrandWrong = brand.isEmpty() || brand.isBlank(),
            isAmountWrong = currentAmount.toIntOrNull() == null || currentAmount.toInt() <= 0,
        )
}