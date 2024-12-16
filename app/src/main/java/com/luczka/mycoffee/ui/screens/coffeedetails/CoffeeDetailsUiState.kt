package com.luczka.mycoffee.ui.screens.coffeedetails

import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.models.ProcessUiState
import com.luczka.mycoffee.ui.models.RoastUiState

sealed interface CoffeeDetailsUiState {
    val isLoading: Boolean
    val isDeleted: Boolean

    data class NoCoffee(
        override val isLoading: Boolean,
        override val isDeleted: Boolean
    ) : CoffeeDetailsUiState

    data class HasCoffee(
        val roasts: List<RoastUiState> = RoastUiState.entries,
        val processes: List<ProcessUiState> = ProcessUiState.entries,
        val coffee: CoffeeUiState,
        override val isLoading: Boolean,
        override val isDeleted: Boolean
    ) : CoffeeDetailsUiState
}