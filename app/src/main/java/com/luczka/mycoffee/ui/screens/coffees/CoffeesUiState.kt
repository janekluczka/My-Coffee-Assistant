package com.luczka.mycoffee.ui.screens.coffees

import com.luczka.mycoffee.domain.models.CoffeeFilter
import com.luczka.mycoffee.ui.models.CoffeeUiState

sealed interface CoffeesUiState {
    object NoCoffees : CoffeesUiState

    data class HasCoffees(
        val coffeeFilters: List<CoffeeFilter>,
        val filteredCoffees: List<CoffeeUiState>,
        val selectedFilter: CoffeeFilter,
    ) : CoffeesUiState
}