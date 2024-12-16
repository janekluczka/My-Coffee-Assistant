package com.luczka.mycoffee.ui.screens.coffees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.ui.mappers.toUiState
import com.luczka.mycoffee.domain.repositories.MyCoffeeDatabaseRepository
import com.luczka.mycoffee.ui.models.CoffeeFilterUiState
import com.luczka.mycoffee.ui.models.CoffeeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SwipeableCoffeeListItemUiState(
    val isRevealed: Boolean = false,
    val coffeeUiState: CoffeeUiState
)

private data class CoffeesViewModelState(
    val swipeableCoffeeListItemUiStates: List<SwipeableCoffeeListItemUiState> = emptyList(),
    val selectedFilter: CoffeeFilterUiState = CoffeeFilterUiState.All,
) {
    fun toCoffeesUiState(): CoffeesUiState {
        return if (swipeableCoffeeListItemUiStates.isEmpty()) {
            CoffeesUiState.NoCoffees
        } else {
            CoffeesUiState.HasCoffees(
                selectedFilter = selectedFilter,
                filteredSwipeableCoffeeListItemUiStates = filterCoffees(),
            )
        }
    }

    private fun filterCoffees(): List<SwipeableCoffeeListItemUiState> {
        return when (selectedFilter) {
            CoffeeFilterUiState.Current -> {
                swipeableCoffeeListItemUiStates.filter { swipeableCoffeeListItemUiState ->
                    val amountFloat = swipeableCoffeeListItemUiState.coffeeUiState.amount.toFloatOrNull()
                    amountFloat != null && amountFloat >= 0.0f
                }
            }

            CoffeeFilterUiState.All -> {
                swipeableCoffeeListItemUiStates
            }

            CoffeeFilterUiState.Favourites -> {
                swipeableCoffeeListItemUiStates.filter { swipeableCoffeeListItemUiState ->
                    swipeableCoffeeListItemUiState.coffeeUiState.isFavourite
                }
            }

            CoffeeFilterUiState.Low -> {
                swipeableCoffeeListItemUiStates.filter { swipeableCoffeeListItemUiState ->
                    val amountFloat = swipeableCoffeeListItemUiState.coffeeUiState.amount.toFloatOrNull()
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
                .collect { coffeeModels ->
                    viewModelState.update {
                        it.copy(swipeableCoffeeListItemUiStates = coffeeModels.map { coffeeModel ->
                            SwipeableCoffeeListItemUiState(coffeeUiState = coffeeModel.toUiState())
                        })
                    }
                }
        }
    }

    fun onAction(action: CoffeesAction) {
        when (action) {
            is CoffeesAction.OnSelectedFilterChanged -> selectedFilterChanged(action.coffeeFilterUiState)
            is CoffeesAction.OnItemActionsExpanded -> collapseOtherItemsActions(action.coffeeId)
            is CoffeesAction.OnCollapseItemActions -> collapseItemsActions(action.coffeeId)
            else -> {}
        }
    }

    private fun selectedFilterChanged(coffeeFilterUiState: CoffeeFilterUiState) {
        viewModelState.update {
            it.copy(selectedFilter = coffeeFilterUiState)
        }
    }

    private fun collapseOtherItemsActions(expandedCoffeeId: Long?) {
        viewModelState.update { currentState ->
            currentState.copy(
                swipeableCoffeeListItemUiStates = currentState.swipeableCoffeeListItemUiStates.map { itemState ->
                    when {
                        itemState.coffeeUiState.coffeeId == expandedCoffeeId -> itemState.copy(isRevealed = true)
                        itemState.isRevealed -> itemState.copy(isRevealed = false)
                        else -> itemState
                    }
                }
            )
        }
    }

    private fun collapseItemsActions(expandedCoffeeId: Long) {
        viewModelState.update { currentState ->
            currentState.copy(
                swipeableCoffeeListItemUiStates = currentState.swipeableCoffeeListItemUiStates.map { itemState ->
                    if (itemState.coffeeUiState.coffeeId == expandedCoffeeId) itemState.copy(isRevealed = false) else itemState
                }
            )
        }
    }

}