package com.luczka.mycoffee.ui.screens.coffees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.domain.mappers.toUiState
import com.luczka.mycoffee.domain.repository.MyCoffeeDatabaseRepository
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

private data class CoffeesViewModelState(
    val allCoffees: List<CoffeeUiState> = emptyList(),
    val selectedFilter: CoffeeFilterUiState = CoffeeFilterUiState.All,
    val coffeeFilterUiStates: List<CoffeeFilterUiState> = CoffeeFilterUiState.entries,
) {
    fun toCoffeesUiState(): CoffeesUiState {
        return if (allCoffees.isEmpty()) {
            CoffeesUiState.NoCoffees
        } else {
            CoffeesUiState.HasCoffees(
                coffeeFilterUiStates = coffeeFilterUiStates,
                selectedFilter = selectedFilter,
                filteredCoffees = filterCoffees(),
            )
        }
    }

    private fun filterCoffees(): List<CoffeeUiState> {
        return when (selectedFilter) {
            CoffeeFilterUiState.Current -> {
                allCoffees.filter { it.hasAmount() }
            }

            CoffeeFilterUiState.All -> {
                allCoffees
            }

            CoffeeFilterUiState.Favourites -> {
                allCoffees.filter { it.isFavourite }
            }

            CoffeeFilterUiState.Low -> {
                allCoffees.filter { it.hasAmountLowerThan(amount = 100f) }
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
            myCoffeeDatabaseRepository.getAllCoffeesStream().collect { coffeeModels ->
                viewModelState.update {
                    it.copy(allCoffees = coffeeModels.map { coffeeModel -> coffeeModel.toUiState() })
                }
            }
        }
    }

    fun onAction(action: CoffeesAction) {
        when (action) {
            is CoffeesAction.OnSelectedFilterChanged -> selectedFilterChanged(action.coffeeFilterUiState)
            else -> {}
        }
    }

    private fun selectedFilterChanged(coffeeFilterUiState: CoffeeFilterUiState) {
        viewModelState.update {
            it.copy(selectedFilter = coffeeFilterUiState)
        }
    }

}