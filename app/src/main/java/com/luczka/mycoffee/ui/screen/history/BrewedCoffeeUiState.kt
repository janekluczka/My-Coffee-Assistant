package com.luczka.mycoffee.ui.screen.history

import com.luczka.mycoffee.ui.model.CoffeeUiState

data class BrewedCoffeeUiState(
    val coffeeAmount: Float,
    val coffee: CoffeeUiState
)