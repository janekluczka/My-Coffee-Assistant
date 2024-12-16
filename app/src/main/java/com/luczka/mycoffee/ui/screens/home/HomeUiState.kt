package com.luczka.mycoffee.ui.screens.home

import com.luczka.mycoffee.ui.models.BrewUiState
import com.luczka.mycoffee.ui.models.CoffeeUiState

sealed interface HomeUiState {
    val recentBrews: List<BrewUiState>
    val recentlyAddedCoffees: List<CoffeeUiState>

    data class HasCoffees(
        override val recentBrews: List<BrewUiState>,
        override val recentlyAddedCoffees: List<CoffeeUiState>,
    ) : HomeUiState
}