package com.coffee.mycoffeeassistant.ui.model.screens

import com.coffee.mycoffeeassistant.ui.model.CoffeeUiState
import com.coffee.mycoffeeassistant.ui.model.components.DropdownMenuItemUiState

data class BrewAssistantUiState(
    val coffeeUiStateList:  List<CoffeeUiState> = emptyList(),
    val dropdownMenuItems: List<DropdownMenuItemUiState<Int>> = emptyList(),
    val selectedCoffee: CoffeeUiState? = null,
    val isCoffeeAmountWrong: Boolean = false,
    val coffeeAmount: String = "",
    val coffeeRatio: String = "",
    val waterAmount: String = "",
)