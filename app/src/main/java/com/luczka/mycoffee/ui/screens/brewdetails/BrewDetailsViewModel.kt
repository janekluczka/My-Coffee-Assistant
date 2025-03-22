package com.luczka.mycoffee.ui.screens.brewdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.domain.usecases.DeleteBrewUseCase
import com.luczka.mycoffee.domain.usecases.GetBrewFlowUseCase
import com.luczka.mycoffee.ui.mappers.toModel
import com.luczka.mycoffee.ui.mappers.toUiState
import com.luczka.mycoffee.ui.models.BrewUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private data class BrewDetailsViewModelState(
    val brew: BrewUiState? = null
) {
    fun toBrewDetailsUiState(): BrewDetailsUiState {
        return BrewDetailsUiState(
            brew = brew
        )
    }
}

@AssistedFactory
interface BrewDetailsViewModelFactory {
    fun create(brewId: Long): BrewDetailsViewModel
}

@HiltViewModel(assistedFactory = BrewDetailsViewModelFactory::class)
class BrewDetailsViewModel @AssistedInject constructor(
    @Assisted brewId: Long,
    private val getBrewFlowUseCase: GetBrewFlowUseCase,
    private val deleteBrewUseCase: DeleteBrewUseCase
) : ViewModel() {

    private val _viewModelState = MutableStateFlow(BrewDetailsViewModelState())
    val uiState = _viewModelState
        .map(BrewDetailsViewModelState::toBrewDetailsUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = _viewModelState.value.toBrewDetailsUiState()
        )

    private val _oneTimeEvent = MutableSharedFlow<BrewDetailsOneTimeEvent>()
    val oneTimeEvent = _oneTimeEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            getBrewFlowUseCase(brewId = brewId).collect { brewModel ->
                _viewModelState.update {
                    it.copy(brew = brewModel?.toUiState())
                }
            }
        }
    }

    fun onAction(action: BrewDetailsAction) {
        when (action) {
            BrewDetailsAction.NavigateUp -> navigateUp()
            BrewDetailsAction.OnDeleteClicked -> deleteBrew()
        }
    }

    private fun navigateUp() {
        viewModelScope.launch {
            _oneTimeEvent.emit(BrewDetailsOneTimeEvent.NavigateUp)
        }
    }

    private fun deleteBrew() {
        val brew = _viewModelState.value.brew ?: return
        viewModelScope.launch {
            deleteBrewUseCase(brew.toModel())
            _oneTimeEvent.emit(BrewDetailsOneTimeEvent.NavigateUp)
        }
    }

}