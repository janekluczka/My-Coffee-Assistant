package com.luczka.mycoffee.ui.screens.historydetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.data.repositories.MyCoffeeDatabaseRepository
import com.luczka.mycoffee.ui.model.BrewUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HistoryDetailsUiState(
    val brew: BrewUiState?
)

private data class BrewDetailsViewModelState(
    val brew: BrewUiState? = null
) {
    fun toBrewDetailsUiState(): HistoryDetailsUiState {
        return HistoryDetailsUiState(
            brew = brew
        )
    }
}

class HistoryDetailsViewModel(
    private val brewId: Int,
    private val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository
) : ViewModel() {

    private val viewModelState = MutableStateFlow(BrewDetailsViewModelState())
    val uiState = viewModelState
        .map(BrewDetailsViewModelState::toBrewDetailsUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toBrewDetailsUiState()
        )

    init {
        viewModelScope.launch {
            myCoffeeDatabaseRepository.getBrewWithCoffees(brewId = brewId).collect { brew ->
                viewModelState.update { it.copy(brew = brew.toBrewUiState()) }
            }
        }
    }

    fun onDelete() {

    }

}