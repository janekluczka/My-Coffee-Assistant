package com.luczka.mycoffee.ui.screens.coffees

import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.models.SwipeableListItemUiState

sealed interface CoffeesUiState {
    data object NoCoffees : CoffeesUiState

    data class HasCoffees(
        val coffeeFilters: List<CoffeeFilterUiState> = CoffeeFilterUiState.entries,
        val selectedCoffeeFilter: CoffeeFilterUiState,
        val coffees: List<SwipeableListItemUiState<CoffeeUiState>>,
    ) : CoffeesUiState
}