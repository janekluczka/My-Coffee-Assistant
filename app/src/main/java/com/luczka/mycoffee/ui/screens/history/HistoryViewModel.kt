package com.luczka.mycoffee.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.domain.mappers.toUiState
import com.luczka.mycoffee.domain.repository.MyCoffeeDatabaseRepository
import com.luczka.mycoffee.ui.models.BrewUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private data class HistoryViewModelState(
    val brewUiStates: List<BrewUiState> = emptyList()
) {
    fun toHistoryUiState(): HistoryUiState {
        return if (brewUiStates.isEmpty()) {
            HistoryUiState.NoBrews
        } else {
            HistoryUiState.HasBrews(brews = brewUiStates)
        }
    }
}

@HiltViewModel
class HistoryViewModel @Inject constructor(
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
            myCoffeeDatabaseRepository.getBrewsWithCoffees().collect { brewModels ->
                val brewUiStates = brewModels
                    .map { it.toUiState() }
                    .sorted()
                viewModelState.update { it.copy(brewUiStates = brewUiStates) }
            }
        }
    }

}