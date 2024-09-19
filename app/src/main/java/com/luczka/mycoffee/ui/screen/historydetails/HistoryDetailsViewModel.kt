package com.luczka.mycoffee.ui.screen.historydetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.domain.repository.MyCoffeeDatabaseRepository
import com.luczka.mycoffee.ui.screen.history.BrewUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
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

@AssistedFactory
interface HistoryDetailsViewModelFactory {
    fun create(brewId: Int): HistoryDetailsViewModel
}

@HiltViewModel(assistedFactory = HistoryDetailsViewModelFactory::class)
class HistoryDetailsViewModel @AssistedInject constructor(
    @Assisted private val brewId: Int,
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
                viewModelState.update { it.copy(brew = brew?.toBrewUiState()) }
            }
        }
    }

    fun onDelete() {
        val brew = viewModelState.value.brew ?: return

        viewModelScope.launch {
            myCoffeeDatabaseRepository.deleteBrew(brew.toBrew())
        }
    }

}