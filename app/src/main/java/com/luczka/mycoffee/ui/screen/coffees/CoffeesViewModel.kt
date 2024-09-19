package com.luczka.mycoffee.ui.screen.coffees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.domain.repository.MyCoffeeDatabaseRepository
import com.luczka.mycoffee.domain.model.CoffeeFilter
import com.luczka.mycoffee.ui.model.CoffeeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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
    val selectedFilter: CoffeeFilter = CoffeeFilter.All,
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
                allCoffees
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