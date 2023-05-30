package com.coffee.mycoffeeassistant.ui.screens.cupboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffee.mycoffeeassistant.data.CoffeeRepository
import com.coffee.mycoffeeassistant.ui.model.CoffeeUiState
import com.coffee.mycoffeeassistant.ui.model.CupboardUiState
import com.coffee.mycoffeeassistant.ui.model.toCoffeeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CupboardViewModel(private val coffeeRepository: CoffeeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CupboardUiState())

    val uiState: StateFlow<CupboardUiState> = _uiState.asStateFlow()

    var allCoffeeUiStateList by mutableStateOf(emptyList<CoffeeUiState>())
        private set

    var favouriteCoffeeUiStateList by mutableStateOf(emptyList<CoffeeUiState>())
        private set

    fun updateCurrentTab(currentTab: Int) {
        _uiState.update { it.copy(currentTab = currentTab) }
    }

    fun getAllCoffees() {
        viewModelScope.launch {
            coffeeRepository.getAllCoffeesStream().collect { coffeeList ->
                allCoffeeUiStateList = coffeeList.map { it.toCoffeeUiState() }
            }
        }
    }

    fun getFavouriteCoffees() {
        viewModelScope.launch {
            coffeeRepository.getFavouriteCoffeesStream().collect { coffeeList ->
                favouriteCoffeeUiStateList = coffeeList.map { it.toCoffeeUiState() }
            }
        }
    }

}