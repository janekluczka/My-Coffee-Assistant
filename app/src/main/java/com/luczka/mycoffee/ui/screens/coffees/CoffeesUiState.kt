package com.luczka.mycoffee.ui.screens.coffees

import com.luczka.mycoffee.ui.models.CoffeeFilterUiState

sealed interface CoffeesUiState {
    data object NoCoffees : CoffeesUiState

    data class HasCoffees(
        val coffeeFilterUiStates: List<CoffeeFilterUiState> = CoffeeFilterUiState.entries,
        val filteredSwipeableCoffeeListItemUiStates: List<SwipeableCoffeeListItemUiState>,
        val selectedFilter: CoffeeFilterUiState,
    ) : CoffeesUiState
}