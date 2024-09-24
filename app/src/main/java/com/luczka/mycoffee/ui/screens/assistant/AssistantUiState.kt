package com.luczka.mycoffee.ui.screens.assistant

import com.luczka.mycoffee.ui.models.CoffeeUiState

sealed interface AssistantUiState {
    val currentCoffees: List<CoffeeUiState>
    val selectedAmountsSum: String
    val waterAmount: String
    val ratioSelectionUiState: RatioSelectionUiState
    val rating: Int?
    val notes: String
    val isFinished: Boolean

    data class NoneSelected(
        override val currentCoffees: List<CoffeeUiState> = emptyList(),
        val amountSelectionUiState: AmountSelectionUiState = AmountSelectionUiState(),
        override val selectedAmountsSum: String = "0.0",
        override val ratioSelectionUiState: RatioSelectionUiState = RatioSelectionUiState(),
        override val waterAmount: String = "0.0",
        override val rating: Int? = null,
        override val notes: String = "",
        override val isFinished: Boolean = false,
    ) : AssistantUiState

    data class CoffeeSelected(
        override val currentCoffees: List<CoffeeUiState> = emptyList(),
        val selectedCoffees: Map<CoffeeUiState, AmountSelectionUiState> = emptyMap(),
        override val selectedAmountsSum: String = "0.0",
        override val ratioSelectionUiState: RatioSelectionUiState = RatioSelectionUiState(),
        override val waterAmount: String = "0.0",
        override val rating: Int? = null,
        override val notes: String = "",
        override val isFinished: Boolean = false
    ) : AssistantUiState
}