package com.luczka.mycoffee.ui.screens.coffees.filter

import com.luczka.mycoffee.ui.models.CoffeeUiState

class FilterLowStrategy : CoffeeFilterStrategy {
    override fun filter(coffees: List<CoffeeUiState>): List<CoffeeUiState> {
        return coffees.filter {
            it.amount.toFloatOrNull()?.let { amount -> amount in 0.0f..100.0f } ?: false
        }
    }
}