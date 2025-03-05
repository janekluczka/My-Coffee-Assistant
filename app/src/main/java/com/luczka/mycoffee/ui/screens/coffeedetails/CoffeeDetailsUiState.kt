package com.luczka.mycoffee.ui.screens.coffeedetails

import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.models.ProcessUiState
import com.luczka.mycoffee.ui.models.RoastUiState

sealed interface CoffeeDetailsUiState {
    val isLoading: Boolean
    val openDeleteDialog: Boolean

    data class NoCoffee(
        override val isLoading: Boolean,
        override val openDeleteDialog: Boolean
    ) : CoffeeDetailsUiState

    data class HasCoffee(
        override val isLoading: Boolean,
        override val openDeleteDialog: Boolean,
        val roasts: List<RoastUiState> = RoastUiState.entries,
        val processes: List<ProcessUiState> = ProcessUiState.entries,
        val coffee: CoffeeUiState,
    ) : CoffeeDetailsUiState
}