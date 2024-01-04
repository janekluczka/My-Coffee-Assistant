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
import java.io.File
import java.time.LocalDate

sealed interface CoffeesUiState {
    val filteredCoffees: List<CoffeeUiState>
    val selectedCoffee: CoffeeUiState?
    val showDetails: Boolean

    data class NoCoffees(
        override val filteredCoffees: List<CoffeeUiState> = emptyList(),
        override val selectedCoffee: CoffeeUiState? = null,
        override val showDetails: Boolean = false,
    ) : CoffeesUiState

    data class HasCoffees(
        override val filteredCoffees: List<CoffeeUiState>,
        val selectedFilter: CoffeeFilter,
        override val selectedCoffee: CoffeeUiState?,
        override val showDetails: Boolean,
    ) : CoffeesUiState
}

private data class CoffeesViewModelState(
    val allCoffees: List<CoffeeUiState> = emptyList(),
    val selectedFilter: CoffeeFilter = CoffeeFilter.Current,
    val filteredCoffees: List<CoffeeUiState> = emptyList(),
    val showDetails: Boolean = false,
    val selectedCoffeeId: Int? = null,
    val wasAddingToFavourites: Boolean = false,
    val wasDeleting: Boolean = false,
) {
    fun toCoffeesUiState(): CoffeesUiState {
        return if (allCoffees.isEmpty()) {
            CoffeesUiState.NoCoffees()
        } else {
            CoffeesUiState.HasCoffees(
                filteredCoffees = filteredCoffees,
                selectedFilter = selectedFilter,
                showDetails = showDetails,
                selectedCoffee = allCoffees.find { it.coffeeId == selectedCoffeeId }
            )
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
                val allCoffees = coffeeList.map { it.toCoffeeUiState() }

                val filteredCoffees = filterCoffees(
                    allCoffees = allCoffees,
                    selectedFilter = viewModelState.value.selectedFilter
                )

                val showDetails = when {
                    viewModelState.value.wasDeleting -> false
                    viewModelState.value.wasAddingToFavourites -> viewModelState.value.showDetails
                    else -> false
                }

                val selectedCoffeeId = when {
                    viewModelState.value.wasDeleting -> null
                    viewModelState.value.wasAddingToFavourites -> viewModelState.value.selectedCoffeeId
                    else -> filteredCoffees.firstOrNull()?.coffeeId
                }

                viewModelState.update {
                    it.copy(
                        allCoffees = allCoffees,
                        filteredCoffees = filteredCoffees,
                        showDetails = showDetails,
                        selectedCoffeeId = selectedCoffeeId,
                        wasAddingToFavourites = false,
                        wasDeleting = false
                    )
                }
            }
        }
    }

    fun onFilterSelected(selectedFilter: CoffeeFilter) {
        val filteredCoffees = filterCoffees(
            allCoffees = viewModelState.value.allCoffees,
            selectedFilter = selectedFilter
        )
        viewModelState.update {
            it.copy(
                selectedFilter = selectedFilter,
                filteredCoffees = filteredCoffees
            )
        }
    }

    private fun filterCoffees(
        allCoffees: List<CoffeeUiState>,
        selectedFilter: CoffeeFilter
    ): List<CoffeeUiState> {
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

    fun onCoffeeSelected(coffeeId: Int?, showDetails: Boolean) {
        viewModelState.update {
            it.copy(
                selectedCoffeeId = coffeeId,
                showDetails = showDetails
            )
        }
    }

    fun onHideDetails() {
        viewModelState.update { it.copy(showDetails = false) }
    }

    fun onUpdateFavourite() {
        val selectedCoffee = viewModelState.value.allCoffees.find {
            it.coffeeId == viewModelState.value.selectedCoffeeId
        } ?: return

        viewModelState.update { it.copy(wasAddingToFavourites = true) }

        viewModelScope.launch {
            val updatedCoffee = selectedCoffee.copy(isFavourite = !selectedCoffee.isFavourite)
            myCoffeeDatabaseRepository.updateCoffee(coffee = updatedCoffee.toCoffee())
        }
    }

    fun onDelete(filesDir: File) {
        val selectedCoffee = viewModelState.value.allCoffees.find {
            it.coffeeId == viewModelState.value.selectedCoffeeId
        } ?: return

        viewModelState.update { it.copy(wasDeleting = true) }

        viewModelScope.launch {
            val imageFile240x240 = selectedCoffee.imageFile240x240
            val imageFile360x360 = selectedCoffee.imageFile360x360
            val imageFile480x480 = selectedCoffee.imageFile480x480
            val imageFile720x720 = selectedCoffee.imageFile720x720
            val imageFile960x960 = selectedCoffee.imageFile960x960

            val file240x240 = imageFile240x240?.let { File(filesDir, it) }
            val file360x360 = imageFile360x360?.let { File(filesDir, it) }
            val file480x480 = imageFile480x480?.let { File(filesDir, it) }
            val file720x720 = imageFile720x720?.let { File(filesDir, it) }
            val file960x960 = imageFile960x960?.let { File(filesDir, it) }

            myCoffeeDatabaseRepository.deleteCoffee(coffee = selectedCoffee.toCoffee())

            file240x240?.delete()
            file360x360?.delete()
            file480x480?.delete()
            file720x720?.delete()
            file960x960?.delete()
        }
    }

}