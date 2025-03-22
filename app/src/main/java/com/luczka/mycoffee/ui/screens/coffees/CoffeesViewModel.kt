package com.luczka.mycoffee.ui.screens.coffees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.domain.usecases.DeleteCoffeeUseCase
import com.luczka.mycoffee.domain.usecases.GetAllCoffeesFlowUseCase
import com.luczka.mycoffee.domain.usecases.UpdateCoffeeUseCase
import com.luczka.mycoffee.ui.mappers.toModel
import com.luczka.mycoffee.ui.mappers.toUiState
import com.luczka.mycoffee.ui.models.CoffeeUiState
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

private data class CoffeesViewModelState(
    val coffees: List<SwipeableListItemUiState<CoffeeUiState>> = emptyList(),
    val selectedCoffeeFilter: CoffeeFilterUiState = CoffeeFilterUiState.All,
    val filteredCoffees: List<SwipeableListItemUiState<CoffeeUiState>> = emptyList()
) {
    fun toCoffeesUiState(): CoffeesUiState {
        return if (coffees.isEmpty()) {
            CoffeesUiState.NoCoffees
        } else {
            CoffeesUiState.HasCoffees(
                selectedCoffeeFilter = selectedCoffeeFilter,
                coffees = filteredCoffees,
            )
        }
    }
}

@HiltViewModel
class CoffeesViewModel @Inject constructor(
    private val getAllCoffeesFlowUseCase: GetAllCoffeesFlowUseCase,
    private val updateCoffeeUseCase: UpdateCoffeeUseCase,
    private val deleteCoffeeUseCase: DeleteCoffeeUseCase
) : ViewModel() {

    private val _viewModelState = MutableStateFlow(CoffeesViewModelState())
    val uiState = _viewModelState
        .map(CoffeesViewModelState::toCoffeesUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = _viewModelState.value.toCoffeesUiState()
        )

    private val _oneTimeEvent = MutableSharedFlow<CoffeesOneTimeEvent>()
    val oneTimeEvent = _oneTimeEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            getAllCoffeesFlowUseCase()
                .map { coffeeModels ->
                    coffeeModels.map { coffeeModel ->
                        SwipeableListItemUiState(item = coffeeModel.toUiState())
                    }
                }
                .collect { swipeableListItemUiStates ->
                    _viewModelState.update {
                        it.copy(coffees = swipeableListItemUiStates)
                    }
                }
        }
    }

    fun onAction(action: CoffeesAction) {
        when (action) {
            CoffeesAction.NavigateUp -> navigateUp()
            CoffeesAction.NavigateToAddCoffee -> navigateToAddCoffee()
            is CoffeesAction.OnEditClicked -> navigateToEdit(action.coffeeId)
            is CoffeesAction.NavigateToCoffeeDetails -> navigateToCoffeeDetails(action.coffeeId)
            is CoffeesAction.OnSelectedFilterChanged -> selectedFilterChanged(action.filter)
            is CoffeesAction.OnItemActionsExpanded -> collapseOtherItemsActions(action.coffeeId)
            is CoffeesAction.OnItemActionsCollapsed -> collapseItemsActions(action.coffeeId)
            is CoffeesAction.OnFavouriteItemClicked -> updateFavourite(action.coffeeId)
            is CoffeesAction.OnDeleteItemClicked -> deleteCoffee(action.coffeeId)
        }
    }

    private fun navigateUp() {
        viewModelScope.launch {
            _oneTimeEvent.emit(CoffeesOneTimeEvent.NavigateUp)
        }
    }

    private fun navigateToAddCoffee() {
        viewModelScope.launch {
            _oneTimeEvent.emit(CoffeesOneTimeEvent.NavigateToAddCoffee)
        }
    }

    private fun navigateToEdit(coffeeId: Long) {
        viewModelScope.launch {
            _oneTimeEvent.emit(CoffeesOneTimeEvent.NavigateToEditCoffee(coffeeId))
        }
    }

    private fun navigateToCoffeeDetails(coffeeId: Long) {
        viewModelScope.launch {
            _oneTimeEvent.emit(CoffeesOneTimeEvent.NavigateToCoffeeDetails(coffeeId))
        }
    }

    private fun selectedFilterChanged(coffeeFilterUiState: CoffeeFilterUiState) {
        _viewModelState.update {
            val filteredCoffees = when (coffeeFilterUiState) {
                CoffeeFilterUiState.Current -> {
                    it.coffees.filter { swipeableCoffeeListItemUiState ->
                        val amountFloat = swipeableCoffeeListItemUiState.item.amount.toFloatOrNull()
                        amountFloat != null && amountFloat >= 0.0f
                    }
                }

                CoffeeFilterUiState.All -> {
                    it.coffees
                }

                CoffeeFilterUiState.Favourites -> {
                    it.coffees.filter { swipeableCoffeeListItemUiState ->
                        swipeableCoffeeListItemUiState.item.isFavourite
                    }
                }

                CoffeeFilterUiState.Low -> {
                    it.coffees.filter { swipeableCoffeeListItemUiState ->
                        val amountFloat = swipeableCoffeeListItemUiState.item.amount.toFloatOrNull()
                        amountFloat != null && amountFloat <= 100.0f && amountFloat >= 0.0f
                    }
                }
            }

            it.copy(
                selectedCoffeeFilter = coffeeFilterUiState,
                filteredCoffees = filteredCoffees
            )
        }
    }


    private fun collapseOtherItemsActions(expandedCoffeeId: Long?) {
        _viewModelState.update { currentState ->
            currentState.copy(
                coffees = currentState.coffees.map { itemState ->
                    when {
                        itemState.item.coffeeId == expandedCoffeeId -> itemState.copy(isRevealed = true)
                        itemState.isRevealed -> itemState.copy(isRevealed = false)
                        else -> itemState
                    }
                }
            )
        }
    }

    private fun collapseItemsActions(collapsedCoffeeId: Long) {
        _viewModelState.update { currentState ->
            currentState.copy(
                coffees = currentState.coffees.map { itemState ->
                    if (itemState.item.coffeeId == collapsedCoffeeId) {
                        itemState.copy(isRevealed = false)
                    } else {
                        itemState
                    }
                }
            )
        }
    }

    private fun updateFavourite(coffeeId: Long) {
        val swipeableListItemUiState = _viewModelState.value.coffees.find { it.item.coffeeId == coffeeId }
        val coffeeUiState = swipeableListItemUiState?.item ?: return
        viewModelScope.launch {
            val updatedCoffeeUiState = coffeeUiState.copy(isFavourite = !coffeeUiState.isFavourite)
            updateCoffeeUseCase(coffeeModel = updatedCoffeeUiState.toModel())
        }
    }

    private fun deleteCoffee(coffeeId: Long) {
        val swipeableListItemUiState = _viewModelState.value.coffees.find { it.item.coffeeId == coffeeId }
        val coffeeUiState = swipeableListItemUiState?.item ?: return
        viewModelScope.launch {
            val updatedCoffeeUiState = coffeeUiState.copy(isFavourite = !coffeeUiState.isFavourite)
            deleteCoffeeUseCase(coffeeModel = updatedCoffeeUiState.toModel())
        }
    }
}