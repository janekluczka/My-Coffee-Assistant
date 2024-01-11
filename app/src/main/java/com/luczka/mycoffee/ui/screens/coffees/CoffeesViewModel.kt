package com.luczka.mycoffee.ui.screens.coffees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.data.repositories.MyCoffeeDatabaseRepository
import com.luczka.mycoffee.enums.CoffeeFilter
import com.luczka.mycoffee.ui.model.CoffeeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

sealed interface CoffeesUiState {
    val filteredCoffees: List<CoffeeUiState>

    data class NoCoffees(
        override val filteredCoffees: List<CoffeeUiState> = emptyList(),
    ) : CoffeesUiState

    data class HasCoffees(
        override val filteredCoffees: List<CoffeeUiState>,
        val selectedFilter: CoffeeFilter,
    ) : CoffeesUiState
}

private data class CoffeesViewModelState(
    val allCoffees: List<CoffeeUiState> = emptyList(),
    val selectedFilter: CoffeeFilter = CoffeeFilter.Current,
) {
    fun toCoffeesUiState(): CoffeesUiState {
        return if (allCoffees.isEmpty()) {
            CoffeesUiState.NoCoffees()
        } else {
            CoffeesUiState.HasCoffees(
                filteredCoffees = filterCoffees(),
                selectedFilter = selectedFilter,
            )
        }
    }

    private fun filterCoffees(): List<CoffeeUiState> {
        return when (selectedFilter) {
            CoffeeFilter.Current -> {
                allCoffees.filter { it.hasAmount() }
            }

            CoffeeFilter.All -> {
                allCoffees
            }

            CoffeeFilter.Favourites -> {
                allCoffees.filter { it.isFavourite }
            }

            CoffeeFilter.Low -> {
                allCoffees.filter { it.hasAmountLowerThan(amount = 100f) }
            }

            CoffeeFilter.Older -> {
                allCoffees.filter { it.isOlderThan(date = LocalDate.now().minusWeeks(4)) }
            }
        }
    }

}

class CoffeesViewModel(
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
            myCoffeeDatabaseRepository.getAllCoffeesStream().collect { coffeeList ->
                viewModelState.update {
                    it.copy(allCoffees = coffeeList.map { coffee -> coffee.toCoffeeUiState() })
                }
            }
        }
    }

    fun onFilterSelected(selectedFilter: CoffeeFilter) {
        viewModelState.update {
            it.copy(selectedFilter = selectedFilter)
        }
    }

}