package com.luczka.mycoffee.ui.screens.coffees.filter

import com.luczka.mycoffee.ui.models.CoffeeUiState

interface CoffeeFilterStrategy {
    fun filter(coffees: List<CoffeeUiState>): List<CoffeeUiState>
}