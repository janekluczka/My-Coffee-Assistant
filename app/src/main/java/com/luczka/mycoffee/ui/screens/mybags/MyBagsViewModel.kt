package com.luczka.mycoffee.ui.screens.mybags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.data.CoffeeRepository
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

sealed interface MyBagsUiState {
    val filteredCoffees: List<CoffeeUiState>
    val selectedCoffee: CoffeeUiState?
    val showDetails: Boolean

    data class NoCoffees(
        override val filteredCoffees: List<CoffeeUiState> = emptyList(),
        override val selectedCoffee: CoffeeUiState? = null,
        override val showDetails: Boolean = false,
    ) : MyBagsUiState

    data class HasCoffees(
        override val filteredCoffees: List<CoffeeUiState>,
        val selectedFilter: CoffeeFilter,
        override val selectedCoffee: CoffeeUiState?,
        override val showDetails: Boolean,
    ) : MyBagsUiState
}

private data class MyBagsViewModelState(
    val allCoffees: List<CoffeeUiState> = emptyList(),
    val selectedFilter: CoffeeFilter = CoffeeFilter.Current,
    val filteredCoffees: List<CoffeeUiState> = emptyList(),
    val showDetails: Boolean = false,
    val selectedCoffeeId: Int? = null,
    val wasAddingToFavourites: Boolean = false,
    val wasDeleting: Boolean = false,
) {
    fun toMyBagsUiState(): MyBagsUiState {
        return if (allCoffees.isEmpty()) {
            MyBagsUiState.NoCoffees()
        } else {
            MyBagsUiState.HasCoffees(
                filteredCoffees = filteredCoffees,
                selectedFilter = selectedFilter,
                showDetails = showDetails,
                selectedCoffee = allCoffees.find { it.id == selectedCoffeeId }
            )
        }
    }
}

class MyBagsViewModel(
    private val coffeeRepository: CoffeeRepository
) : ViewModel() {

    private val viewModelState = MutableStateFlow(MyBagsViewModelState())
    val uiState = viewModelState
        .map(MyBagsViewModelState::toMyBagsUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toMyBagsUiState()
        )

    init {
        viewModelScope.launch {
            coffeeRepository.getAllCoffeesStream().collect { coffeeList ->
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
                    else -> filteredCoffees.firstOrNull()?.id
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
            it.id == viewModelState.value.selectedCoffeeId
        } ?: return

        viewModelState.update { it.copy(wasAddingToFavourites = true) }

        viewModelScope.launch {
            val updatedCoffee = selectedCoffee.copy(isFavourite = !selectedCoffee.isFavourite)
            coffeeRepository.updateCoffee(coffee = updatedCoffee.toCoffee())
        }
    }

    fun onDelete(filesDir: File) {
        val selectedCoffee = viewModelState.value.allCoffees.find {
            it.id == viewModelState.value.selectedCoffeeId
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

            coffeeRepository.deleteCoffee(coffee = selectedCoffee.toCoffee())

            file240x240?.delete()
            file360x360?.delete()
            file480x480?.delete()
            file720x720?.delete()
            file960x960?.delete()
        }
    }

}