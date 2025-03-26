package com.luczka.mycoffee.ui.screens.coffees.filter

import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.models.SwipeableListItemUiState

interface CoffeeFilterStrategy {
    fun filter(coffees: List<SwipeableListItemUiState<CoffeeUiState>>): List<SwipeableListItemUiState<CoffeeUiState>>
}