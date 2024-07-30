package com.luczka.mycoffee.ui.screen.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.data.repository.MyCoffeeDatabaseRepository
import com.luczka.mycoffee.ui.model.BrewUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface HistoryUiState {
    val brews: List<BrewUiState>

    data class NoBrews(
        override val brews: List<BrewUiState> = emptyList()
    ) : HistoryUiState

    data class HasBrews(
        override val brews: List<BrewUiState>
    ) : HistoryUiState
}

private data class HistoryViewModelState(
    val brews: List<BrewUiState> = emptyList()
) {
    fun toHistoryUiState(): HistoryUiState {
        return if (brews.isEmpty()) {
            HistoryUiState.NoBrews()
        } else {
            HistoryUiState.HasBrews(brews = brews)
        }
    }
}

class HistoryViewModel(
    private val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository
) : ViewModel() {

    private val viewModelState = MutableStateFlow(HistoryViewModelState())
    val uiState = viewModelState
        .map(HistoryViewModelState::toHistoryUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toHistoryUiState()
        )

    init {
        viewModelScope.launch {
            myCoffeeDatabaseRepository.getBrewsWithCoffees().collect { brewList ->
                val brews = brewList.map { it.toBrewUiState() }.sortedByDescending { it.brewId }
                viewModelState.update { it.copy(brews = brews) }
            }
        }
    }

}