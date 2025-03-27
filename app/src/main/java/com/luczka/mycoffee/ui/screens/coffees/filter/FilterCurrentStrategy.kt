package com.luczka.mycoffee.ui.screens.coffees.filter

import com.luczka.mycoffee.ui.models.CoffeeUiState

class FilterCurrentStrategy : CoffeeFilterStrategy {
    override fun filter(coffees: List<CoffeeUiState>): List<CoffeeUiState> {
        return coffees.filter {
            it.amount.toFloatOrNull()?.let { amount -> amount > 0.0f } ?: false
        }
    }
}