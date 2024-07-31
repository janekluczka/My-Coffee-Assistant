package com.luczka.mycoffee.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.data.repository.MyCoffeeDatabaseRepository
import com.luczka.mycoffee.ui.model.CoffeeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    val lowAmountCoffees: List<CoffeeUiState>
    val hasMoreLowAmountCoffees: Boolean

    data class HasCoffees(
        override val lowAmountCoffees: List<CoffeeUiState>,
        override val hasMoreLowAmountCoffees: Boolean,
    ) : HomeUiState
}

private data class HomeViewModelState(
    val currentCoffees: List<CoffeeUiState> = emptyList(),
) {
    fun toHomeUiState(): HomeUiState {
        val rowItemsAmount = 4
        val lowAmountCoffees = currentCoffees.filter { it.hasAmountLowerThan(100f) }

        return HomeUiState.HasCoffees(
            lowAmountCoffees = lowAmountCoffees.take(rowItemsAmount),
            hasMoreLowAmountCoffees = lowAmountCoffees.size > rowItemsAmount,
        )
    }
}

class HomeViewModel(
    private val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository
) : ViewModel() {

    private val viewModelState = MutableStateFlow(HomeViewModelState())
    val uiState = viewModelState
        .map(HomeViewModelState::toHomeUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toHomeUiState()
        )

    init {
        viewModelScope.launch {
            myCoffeeDatabaseRepository.getCurrentCoffeesStream().collect { coffeeList ->
                val currentCoffees = coffeeList.map { it.toCoffeeUiState() }
                viewModelState.update {
                    it.copy(currentCoffees = currentCoffees)
                }
            }
        }
    }
}