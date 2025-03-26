package com.luczka.mycoffee.ui.screens.coffees.filter

import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.models.SwipeableListItemUiState

class FilterLowStrategy : CoffeeFilterStrategy {
    override fun filter(coffees: List<SwipeableListItemUiState<CoffeeUiState>>): List<SwipeableListItemUiState<CoffeeUiState>> {
        return coffees.filter {
            it.item.amount.toFloatOrNull()?.let { amount -> amount in 0.0f..100.0f } ?: false
        }
    }
}