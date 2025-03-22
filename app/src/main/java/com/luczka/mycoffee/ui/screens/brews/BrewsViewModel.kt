package com.luczka.mycoffee.ui.screens.brews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.domain.usecases.DeleteBrewUseCase
import com.luczka.mycoffee.domain.usecases.GetAllBrewsFlowUseCase
import com.luczka.mycoffee.ui.mappers.toModel
import com.luczka.mycoffee.ui.mappers.toUiState
import com.luczka.mycoffee.ui.models.BrewUiState
import com.luczka.mycoffee.ui.models.SwipeableListItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private data class BrewsViewModelState(
    val selectedFilter: BrewFilterUiState = BrewFilterUiState.LATEST,
    val brews: List<SwipeableListItemUiState<BrewUiState>> = emptyList()
) {
    fun toBrewsUiState(): BrewsUiState {
        return if (brews.isEmpty()) {
            BrewsUiState.NoBrews
        } else {
            BrewsUiState.HasBrews(
                selectedFilter = selectedFilter,
                brews = filterBrews()
            )
        }
    }

    private fun filterBrews(): List<SwipeableListItemUiState<BrewUiState>> {
        return when (selectedFilter) {
            BrewFilterUiState.LATEST -> {
                brews
            }

            BrewFilterUiState.BEST_RATED -> {
                brews.sortedByDescending { it.item.rating }
            }
        }
    }
}

@HiltViewModel
class BrewsViewModel @Inject constructor(
    private val getAllBrewsFlowUseCase: GetAllBrewsFlowUseCase,
    private val deleteBrewUseCase: DeleteBrewUseCase
) : ViewModel() {

    private val _viewModelState = MutableStateFlow(BrewsViewModelState())
    val uiState = _viewModelState
        .map(BrewsViewModelState::toBrewsUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = _viewModelState.value.toBrewsUiState()
        )

    private val _oneTimeEvent = MutableSharedFlow<BrewsOneTimeEvent>()
    val oneTimeEvent = _oneTimeEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            getAllBrewsFlowUseCase()
                .map { brewModels ->
                    brewModels.map { brewModel ->
                        SwipeableListItemUiState(item = brewModel.toUiState())
                    }
                }
                .collect { swipeableListItemUiStates ->
                    _viewModelState.update {
                        it.copy(brews = swipeableListItemUiStates)
                    }
                }
        }
    }

    fun onAction(action: BrewsAction) {
        when (action) {
            BrewsAction.NavigateToAssistant -> navigateToAssistant()
            is BrewsAction.NavigateToBrewDetails -> navigateToBrewDetails(action.brewId)
            is BrewsAction.OnSelectedFilterChanged -> selectFilter(action.brewFilter)
            is BrewsAction.OnItemActionsExpanded -> collapseOtherItemsActions(action.brewId)
            is BrewsAction.OnItemActionsCollapsed -> collapseItemsActions(action.brewId)
            is BrewsAction.OnDeleteClicked -> deleteBrew(action.brewId)
        }
    }

    private fun navigateToAssistant() {
        viewModelScope.launch {
            _oneTimeEvent.emit(BrewsOneTimeEvent.NavigateToAssistant)
        }
    }

    private fun navigateToBrewDetails(brewId: Long) {
        viewModelScope.launch {
            _oneTimeEvent.emit(BrewsOneTimeEvent.NavigateToBrewDetails(brewId))
        }
    }

    private fun selectFilter(brewFilter: BrewFilterUiState) {
        _viewModelState.update {
            it.copy(selectedFilter = brewFilter)
        }
    }

    private fun collapseOtherItemsActions(expandedBrewId: Long?) {
        _viewModelState.update { currentState ->
            currentState.copy(
                brews = currentState.brews.map { itemState ->
                    when {
                        itemState.item.brewId == expandedBrewId -> itemState.copy(isRevealed = true)
                        itemState.isRevealed -> itemState.copy(isRevealed = false)
                        else -> itemState
                    }
                }
            )
        }
    }

    private fun collapseItemsActions(collapsedBrewId: Long) {
        _viewModelState.update { currentState ->
            currentState.copy(
                brews = currentState.brews.map { itemState ->
                    if (itemState.item.brewId == collapsedBrewId) {
                        itemState.copy(isRevealed = false)
                    } else {
                        itemState
                    }
                }
            )
        }
    }

    private fun deleteBrew(brewId: Long) {
        val swipeableListItemUiState = _viewModelState.value.brews.find { it.item.brewId == brewId }
        val brewUiState = swipeableListItemUiState?.item ?: return
        viewModelScope.launch {
            deleteBrewUseCase(brewUiState.toModel())
        }
    }
}