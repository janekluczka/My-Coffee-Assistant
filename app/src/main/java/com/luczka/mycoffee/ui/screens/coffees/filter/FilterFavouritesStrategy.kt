package com.luczka.mycoffee.ui.screens.coffees.filter

import com.luczka.mycoffee.ui.models.CoffeeUiState

class FilterFavouritesStrategy : CoffeeFilterStrategy {
    override fun filter(coffees: List<CoffeeUiState>): List<CoffeeUiState> {
        return coffees.filter { it.isFavourite }
    }
}