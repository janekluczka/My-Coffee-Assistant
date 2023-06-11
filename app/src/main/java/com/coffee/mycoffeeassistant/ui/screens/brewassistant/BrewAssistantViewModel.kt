package com.coffee.mycoffeeassistant.ui.screens.brewassistant

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffee.mycoffeeassistant.data.CoffeeRepository
import com.coffee.mycoffeeassistant.ui.model.BrewAssistantUiState
import com.coffee.mycoffeeassistant.ui.model.CoffeeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BrewAssistantViewModel(private val coffeeRepository: CoffeeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(BrewAssistantUiState())

    val uiState: StateFlow<BrewAssistantUiState> = _uiState.asStateFlow()

    var coffeeUiStateList by mutableStateOf(emptyList<CoffeeUiState>())
        private set

    fun getCoffees() {
        viewModelScope.launch {
            coffeeRepository.getAllCoffeesStream().collect { coffeeList ->
                coffeeUiStateList = coffeeList
                    .map { it.toCoffeeUiState() }
                    .sorted()
            }
        }
    }

    fun selectCoffee(coffeeUiState: CoffeeUiState) {
        _uiState.update { it.copy(isCoffeeSelected = true, selectedCoffee = coffeeUiState) }
    }

    private fun unselectCoffee() {
        _uiState.update { it.copy(isCoffeeSelected = false, selectedCoffee = null) }
    }

    fun reduceAmount(selectedAmount: Float, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val selectedCoffee = _uiState.value.selectedCoffee
            if (selectedCoffee != null) {
                val currentAmount = selectedCoffee.currentAmount.toFloat()
                val isFavourite = selectedCoffee.isFavourite
                if (currentAmount <= selectedAmount && !isFavourite) {
                    coffeeRepository.deleteCoffee(selectedCoffee.toCoffee())
                    unselectCoffee()
                } else {
                    val updatedAmount = currentAmount - selectedAmount
                    val updatedCoffee =
                        selectedCoffee.copy(currentAmount = updatedAmount.toString())
                    selectCoffee(updatedCoffee)
                    coffeeRepository.updateCoffee(updatedCoffee.toCoffee())
                }
            }
            onSuccess()
            getCoffees()
        }
    }

}