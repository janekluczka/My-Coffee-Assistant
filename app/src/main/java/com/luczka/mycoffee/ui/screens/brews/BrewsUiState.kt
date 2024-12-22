package com.luczka.mycoffee.ui.screens.brews

import com.luczka.mycoffee.ui.models.BrewUiState
import com.luczka.mycoffee.ui.models.SwipeableListItemUiState

sealed interface BrewsUiState {
    data object NoBrews : BrewsUiState

    data class HasBrews(
        val brewFilters: List<BrewFilterUiState> = BrewFilterUiState.entries,
        val selectedFilter: BrewFilterUiState,
        val brews: List<SwipeableListItemUiState<BrewUiState>>
    ) : BrewsUiState
}