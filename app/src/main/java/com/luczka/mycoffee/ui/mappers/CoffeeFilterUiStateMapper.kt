package com.luczka.mycoffee.ui.mappers

import com.luczka.mycoffee.domain.models.CoffeeFilterModel
import com.luczka.mycoffee.ui.screens.coffees.CoffeeFilterUiState

fun CoffeeFilterUiState.toModel() : CoffeeFilterModel {
    return when (this) {
        CoffeeFilterUiState.All -> CoffeeFilterModel.All
        CoffeeFilterUiState.Favourites -> CoffeeFilterModel.Favourites
    }
}