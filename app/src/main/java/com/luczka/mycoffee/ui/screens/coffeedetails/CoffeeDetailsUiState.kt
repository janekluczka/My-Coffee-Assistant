package com.luczka.mycoffee.ui.screens.coffeedetails

import com.luczka.mycoffee.ui.models.CoffeeUiState

sealed interface CoffeeDetailsUiState {
    data class NoCoffee(
        val coffeeId: Int,
        val isDeleted: Boolean
    ) : CoffeeDetailsUiState

    data class HasCoffee(
        val coffee: CoffeeUiState
    ) : CoffeeDetailsUiState
}