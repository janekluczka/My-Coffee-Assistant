package com.luczka.mycoffee.ui.screens.coffees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.domain.repositories.MyCoffeeDatabaseRepository
import com.luczka.mycoffee.ui.mappers.toModel
import com.luczka.mycoffee.ui.mappers.toUiState
import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.models.SwipeableListItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private data class CoffeesViewModelState(
    val coffees: List<SwipeableListItemUiState<CoffeeUiState>> = emptyList(),
    val selectedCoffeeFilter: CoffeeFilterUiState = CoffeeFilterUiState.All,
) {
    fun toCoffeesUiState(): CoffeesUiState {
        return if (coffees.isEmpty()) {
            CoffeesUiState.NoCoffees
        } else {
            CoffeesUiState.HasCoffees(
                selectedCoffeeFilter = selectedCoffeeFilter,
                coffees = filterCoffees(),
            )
        }
    }

    private fun filterCoffees(): List<SwipeableListItemUiState<CoffeeUiState>> {
        return when (selectedCoffeeFilter) {
            CoffeeFilterUiState.Current -> {
                coffees.filter { swipeableCoffeeListItemUiState ->
                    val amountFloat = swipeableCoffeeListItemUiState.item.amount.toFloatOrNull()
                    amountFloat != null && amountFloat >= 0.0f
                }
            }

            CoffeeFilterUiState.All -> {
                coffees
            }

            CoffeeFilterUiState.Favourites -> {
                coffees.filter { swipeableCoffeeListItemUiState ->
                    swipeableCoffeeListItemUiState.item.isFavourite
                }
            }

            CoffeeFilterUiState.Low -> {
                coffees.filter { swipeableCoffeeListItemUiState ->
                    val amountFloat = swipeableCoffeeListItemUiState.item.amount.toFloatOrNull()
                    amountFloat != null && amountFloat <= 100.0f && amountFloat >= 0.0f
                }
            }
        }
    }
}

@HiltViewModel
class CoffeesViewModel @Inject constructor(
    private val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository
) : ViewModel() {

    private val viewModelState = MutableStateFlow(CoffeesViewModelState())
    val uiState = viewModelState
        .map(CoffeesViewModelState::toCoffeesUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toCoffeesUiState()
        )

    init {
        viewModelScope.launch {
            myCoffeeDatabaseRepository
                .getAllCoffeesFlow()
                .map { coffeeModels ->
                    coffeeModels.map { coffeeModel ->
                        SwipeableListItemUiState(item = coffeeModel.toUiState())
                    }
                }
                .collect { swipeableListItemUiStates ->
                    viewModelState.update {
                        it.copy(coffees = swipeableListItemUiStates)
                    }
                }
        }
    }

    fun onAction(action: CoffeesAction) {
        when (action) {
            is CoffeesAction.NavigateUp -> {}
            is CoffeesAction.NavigateToAddCoffee -> {}
            is CoffeesAction.OnEditClicked -> {}
            is CoffeesAction.NavigateToCoffeeDetails -> {}

            is CoffeesAction.OnSelectedFilterChanged -> selectedFilterChanged(action.filter)
            is CoffeesAction.OnItemActionsExpanded -> collapseOtherItemsActions(action.coffeeId)
            is CoffeesAction.OnItemActionsCollapsed -> collapseItemsActions(action.coffeeId)
            is CoffeesAction.OnFavouriteItemClicked -> updateFavourite(action.coffeeId)
            is CoffeesAction.OnDeleteItemClicked -> deleteCoffee(action.coffeeId)
        }
    }

    private fun selectedFilterChanged(coffeeFilterUiState: CoffeeFilterUiState) {
        viewModelState.update {
            it.copy(selectedCoffeeFilter = coffeeFilterUiState)
        }
    }

    private fun collapseOtherItemsActions(expandedCoffeeId: Long?) {
        viewModelState.update { currentState ->
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
        viewModelState.update { currentState ->
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
        val swipeableListItemUiState = viewModelState.value.coffees.find { it.item.coffeeId == coffeeId }
        val coffeeUiState = swipeableListItemUiState?.item ?: return
        viewModelScope.launch {
            val updatedCoffeeUiState = coffeeUiState.copy(isFavourite = !coffeeUiState.isFavourite)
            myCoffeeDatabaseRepository.updateCoffeeOld(coffeeModel = updatedCoffeeUiState.toModel())
        }
    }

    private fun deleteCoffee(coffeeId: Long) {
        val swipeableListItemUiState = viewModelState.value.coffees.find { it.item.coffeeId == coffeeId }
        val coffeeUiState = swipeableListItemUiState?.item ?: return
        viewModelScope.launch {
            val updatedCoffeeUiState = coffeeUiState.copy(isFavourite = !coffeeUiState.isFavourite)
            myCoffeeDatabaseRepository.deleteCoffee(coffeeModel = updatedCoffeeUiState.toModel())
        }
    }

}