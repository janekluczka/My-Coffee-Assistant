package com.coffee.mycoffeeassistant.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffee.mycoffeeassistant.data.CoffeeRepository
import com.coffee.mycoffeeassistant.ui.model.CoffeeUiState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeViewModel(private val coffeeRepository: CoffeeRepository) : ViewModel() {

    private val maxLowAmount = 70.0f
    private val maxRoastingDate = LocalDate.now().minusWeeks(4).format(DateTimeFormatter.BASIC_ISO_DATE)

    var lowAmountCoffeeUiStateList by mutableStateOf(emptyList<CoffeeUiState>())
        private set

    var oldCoffeeUiStateList by mutableStateOf(emptyList<CoffeeUiState>())
        private set

    fun getLowAmountCoffees() {
        viewModelScope.launch {
            coffeeRepository.getAmountLowerThanStream(maxLowAmount).collect { coffeeList ->
                lowAmountCoffeeUiStateList = coffeeList.map { it.toCoffeeUiState() }
            }
        }
    }

    fun getOldCoffees() {
        viewModelScope.launch {
            coffeeRepository.getOlderThanStream(maxRoastingDate).collect { coffeeList ->
                oldCoffeeUiStateList = coffeeList.map { it.toCoffeeUiState() }
            }
        }
    }

}