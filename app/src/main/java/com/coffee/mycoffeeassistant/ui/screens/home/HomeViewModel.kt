package com.coffee.mycoffeeassistant.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffee.mycoffeeassistant.data.CoffeeRepository
import com.coffee.mycoffeeassistant.ui.model.screens.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomeViewModel(private val coffeeRepository: CoffeeRepository) : ViewModel() {

    companion object {
        private const val MAX_LOW_AMOUNT = 70.0f
        private val MAX_ROASTING_DATE =
            LocalDate.now()
                .minusWeeks(4)
                .format(DateTimeFormatter.BASIC_ISO_DATE)
    }

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            coffeeRepository.getAmountLowerThanStream(MAX_LOW_AMOUNT).collect { coffeeList ->
                val coffeeCardUiStateList = coffeeList.map { it.toCoffeeCardUiState() }
                _uiState.update { it.copy(lowAmountCoffeeUiStateList = coffeeCardUiStateList) }
            }
        }
        viewModelScope.launch {
            coffeeRepository.getOlderThanStream(MAX_ROASTING_DATE).collect { coffeeList ->
                val coffeeCardUiStateList = coffeeList.map { it.toCoffeeCardUiState() }
                _uiState.update { it.copy(oldCoffeeUiStateList = coffeeCardUiStateList) }
            }
        }
    }

}