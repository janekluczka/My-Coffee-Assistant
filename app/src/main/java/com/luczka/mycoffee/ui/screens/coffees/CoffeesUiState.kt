package com.luczka.mycoffee.ui.screens.coffees

import com.luczka.mycoffee.ui.models.CoffeeFilterUiState
import com.luczka.mycoffee.ui.models.CoffeeUiState

sealed interface CoffeesUiState {
    object NoCoffees : CoffeesUiState

    data class HasCoffees(
        val coffeeFilterUiStates: List<CoffeeFilterUiState>,
        val filteredCoffees: List<CoffeeUiState>,
        val selectedFilter: CoffeeFilterUiState,
    ) : CoffeesUiState
}