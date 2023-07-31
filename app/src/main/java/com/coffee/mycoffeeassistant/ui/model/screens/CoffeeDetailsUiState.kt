package com.coffee.mycoffeeassistant.ui.model.screens

import com.coffee.mycoffeeassistant.ui.model.CoffeeUiState

data class CoffeeDetailsUiState(
    val openRemoveFromFavouritesDialog: Boolean = false,
    val openDeleteDialog: Boolean = false,
    val coffeeUiState: CoffeeUiState = CoffeeUiState()
)