package com.coffee.mycoffeeassistant.ui.screens.addcoffee

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.coffee.mycoffeeassistant.data.CoffeeRepository
import com.coffee.mycoffeeassistant.ui.model.AddCoffeeUiState
import com.coffee.mycoffeeassistant.ui.model.CoffeeUiState
import com.coffee.mycoffeeassistant.ui.model.markWrongFields
import com.coffee.mycoffeeassistant.ui.model.toCoffee
import com.coffee.mycoffeeassistant.ui.model.isValid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AddCoffeeViewModel(private val coffeeRepository: CoffeeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AddCoffeeUiState())

    val uiState: StateFlow<AddCoffeeUiState> = _uiState.asStateFlow()

    var coffeeUiState by mutableStateOf(CoffeeUiState())
        private set

    fun updateCoffeeUiState(newCoffeeUiState: CoffeeUiState) {
        coffeeUiState = newCoffeeUiState.copy()
    }

    suspend fun saveCoffee() {
        _uiState.update {
            coffeeUiState.markWrongFields()
        }
        if(coffeeUiState.isValid()) {
            coffeeRepository.insertCoffee(coffeeUiState.toCoffee())
        }
    }
}