package com.luczka.mycoffee.ui.screens.history

import com.luczka.mycoffee.ui.models.BrewUiState

sealed interface HistoryUiState {
    object NoBrews : HistoryUiState
    data class HasBrews(val brews: List<BrewUiState>) : HistoryUiState
}