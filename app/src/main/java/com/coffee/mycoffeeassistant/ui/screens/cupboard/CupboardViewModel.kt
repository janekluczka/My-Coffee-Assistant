package com.coffee.mycoffeeassistant.ui.screens.cupboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffee.mycoffeeassistant.data.CoffeeRepository
import com.coffee.mycoffeeassistant.ui.model.screens.CupboardUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CupboardViewModel(
    private val coffeeRepository: CoffeeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CupboardUiState())
    val uiState: StateFlow<CupboardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            coffeeRepository.getInStockCoffeesStream().collect { coffeeList ->
                val coffeeCardUiStateList = coffeeList.map { it.toCoffeeCardUiState() }
                _uiState.update { it.copy(inStockCoffeeUiStateList = coffeeCardUiStateList) }
            }
        }
        viewModelScope.launch {
            coffeeRepository.getFavouriteCoffeesStream().collect { coffeeList ->
                val coffeeCardUiStateList = coffeeList.map { it.toCoffeeCardUiState() }
                _uiState.update { it.copy(favouriteCoffeeUiStateList = coffeeCardUiStateList) }
            }
        }
    }

    fun updateUiState(newCupboardUiState: CupboardUiState) {
        viewModelScope.launch {
            _uiState.update {
                newCupboardUiState.copy()
            }
        }
    }

}