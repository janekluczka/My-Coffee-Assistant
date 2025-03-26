package com.luczka.mycoffee.ui.screens.coffees.filter

import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.models.SwipeableListItemUiState

class FilterAllStrategy : CoffeeFilterStrategy {
    override fun filter(coffees: List<SwipeableListItemUiState<CoffeeUiState>>): List<SwipeableListItemUiState<CoffeeUiState>> {
        return coffees
    }
}