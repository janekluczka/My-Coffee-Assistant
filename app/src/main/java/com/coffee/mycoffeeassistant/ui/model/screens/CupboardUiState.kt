package com.coffee.mycoffeeassistant.ui.model.screens

import com.coffee.mycoffeeassistant.ui.model.components.CoffeeCardUiState

data class CupboardUiState(
    val currentTab: Int = 0,
    val inStockCoffeeUiStateList: List<CoffeeCardUiState> = emptyList(),
    val favouriteCoffeeUiStateList:  List<CoffeeCardUiState> = emptyList(),
)