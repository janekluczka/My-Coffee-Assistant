package com.luczka.mycoffee.ui.screens.historydetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.domain.mappers.toModel
import com.luczka.mycoffee.domain.mappers.toUiState
import com.luczka.mycoffee.domain.repository.MyCoffeeDatabaseRepository
import com.luczka.mycoffee.ui.models.BrewUiState
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
    // TODO: Add HasData, NoData, isDeleted?
)

private data class HistoryDetailsViewModelState(
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
    @Assisted brewId: Int,
    private val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository
) : ViewModel() {

    private val viewModelState = MutableStateFlow(HistoryDetailsViewModelState())
    val uiState = viewModelState
        .map(HistoryDetailsViewModelState::toBrewDetailsUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toBrewDetailsUiState()
        )

    init {
        viewModelScope.launch {
            myCoffeeDatabaseRepository.getBrewWithCoffees(brewId = brewId).collect { brewModel ->
                viewModelState.update { it.copy(brew = brewModel?.toUiState()) }
            }
        }
    }

    fun onAction(action: HistoryDetailsAction) {
        when (action) {
            HistoryDetailsAction.OnDeleteClicked -> delete()
            else -> {}
        }
    }

    fun delete() {
        val brew = viewModelState.value.brew ?: return

        viewModelScope.launch {
            myCoffeeDatabaseRepository.deleteBrew(brew.toModel())
        }
    }

}