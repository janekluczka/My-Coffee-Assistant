package com.coffee.mycoffeeassistant.ui.screens.coffeedetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffee.mycoffeeassistant.data.CoffeeRepository
import com.coffee.mycoffeeassistant.ui.model.CoffeeUiState
import com.coffee.mycoffeeassistant.ui.model.toCoffeeUiState
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class CoffeeDetailsViewModel(private val coffeeRepository: CoffeeRepository) : ViewModel() {

    var coffeeUiState by mutableStateOf(CoffeeUiState())
        private set

    fun getCoffeeUiState(id: Int) {
        viewModelScope.launch {
            coffeeRepository.getCoffeeStream(id).filterNotNull().collect { coffee ->
                coffeeUiState = coffee.toCoffeeUiState()
            }
        }

    }

}