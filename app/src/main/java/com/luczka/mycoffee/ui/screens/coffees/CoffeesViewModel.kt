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
import com.luczka.mycoffee.ui.screens.coffees.filter.CoffeeFilterStrategy
import com.luczka.mycoffee.ui.screens.coffees.filter.FilterAllStrategy
import com.luczka.mycoffee.ui.screens.coffees.filter.FilterCurrentStrategy
import com.luczka.mycoffee.ui.screens.coffees.filter.FilterFavouritesStrategy
import com.luczka.mycoffee.ui.screens.coffees.filter.FilterLowStrategy
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
    private val currentFilterStrategy: CoffeeFilterStrategy = FilterAllStrategy()
) {
    val selectedCoffeeFilter: CoffeeFilterUiState
        get() = when (currentFilterStrategy) {
            is FilterAllStrategy -> CoffeeFilterUiState.All
            is FilterCurrentStrategy -> CoffeeFilterUiState.Current
            is FilterFavouritesStrategy -> CoffeeFilterUiState.Favourites
            is FilterLowStrategy -> CoffeeFilterUiState.Low
            else -> CoffeeFilterUiState.All
        }

    fun toCoffeesUiState(): CoffeesUiState {
        if (coffees.isEmpty()) return CoffeesUiState.NoCoffees

        return CoffeesUiState.HasCoffees(
            selectedCoffeeFilter = selectedCoffeeFilter,
            coffees = currentFilterStrategy.filter(coffees),
        )
    }

    fun updateFilterStrategy(filter: CoffeeFilterUiState): CoffeesViewModelState {
        val newStrategy: CoffeeFilterStrategy = when (filter) {
            CoffeeFilterUiState.All -> FilterAllStrategy()
            CoffeeFilterUiState.Current -> FilterCurrentStrategy()
            CoffeeFilterUiState.Favourites -> FilterFavouritesStrategy()
            CoffeeFilterUiState.Low -> FilterLowStrategy()
        }
        return copy(currentFilterStrategy = newStrategy)
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
            it.updateFilterStrategy(coffeeFilterUiState)
        }
    }

    private fun collapseOtherItemsActions(coffeeId: Long?) {
        updateItemActionsVisibility(itemId = coffeeId, isRevealed = true)
    }

    private fun collapseItemsActions(coffeeId: Long) {
        updateItemActionsVisibility(itemId = coffeeId, isRevealed = false)
    }

    private fun updateItemActionsVisibility(itemId: Long?, isRevealed: Boolean) {
        _viewModelState.update { viewModelState ->
            viewModelState.copy(
                coffees = viewModelState.coffees.map { swipeableListItemUiState ->
                    when {
                        itemId != null && swipeableListItemUiState.item.coffeeId == itemId -> swipeableListItemUiState.copy(isRevealed = isRevealed)
                        !isRevealed && swipeableListItemUiState.isRevealed -> swipeableListItemUiState.copy(isRevealed = false)
                        else -> swipeableListItemUiState
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