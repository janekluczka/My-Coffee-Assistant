package com.coffee.mycoffeeassistant.ui.model

import com.coffee.mycoffeeassistant.data.Coffee
import java.time.LocalDate

data class CoffeeUiState(
    val roastOptions: List<String> = listOf("Unknown", "Light", "Medium", "Medium Dark", "Dark"),
    val processOptions: List<String> = listOf("Unknown", "Natural", "Washed", "Honey"),
    val id: Int = 0,
    val name: String = "",
    val brand: String = "",
    val currentAmount: String = "",
    val startAmount: String = "",
    val roast: String = roastOptions[0],
    val process: String = processOptions[0],
    val roastingDate: LocalDate = LocalDate.now()
)

fun CoffeeUiState.toCoffee(): Coffee = Coffee(
    id = id,
    name = name,
    brand = brand,
    currentAmount = currentAmount.toFloat(),
    startAmount = startAmount.toFloat(),
    roast = roast,
    process = process
)

fun Coffee.toCoffeeUiState(): CoffeeUiState = CoffeeUiState(
    id = id,
    name = name,
    brand = brand,
    currentAmount = currentAmount.toString(),
    startAmount = startAmount.toString(),
    roast = roast,
    process = process
)

fun CoffeeUiState.markWrongFields(): AddCoffeeUiState = AddCoffeeUiState(
    isNameWrong = name.isEmpty() || name.isBlank(),
    isBrandWrong = brand.isEmpty() || brand.isBlank(),
    isAmountWrong = currentAmount.toIntOrNull() == null || currentAmount.toInt() <= 0,
)

fun CoffeeUiState.isValid(): Boolean =
    name.isNotEmpty() &&
            name.isNotBlank() &&
            brand.isNotEmpty() &&
            brand.isNotBlank() &&
            currentAmount.toIntOrNull() != null &&
            currentAmount.toInt() > 0

