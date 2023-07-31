package com.coffee.mycoffeeassistant.ui.screens.brewassistant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coffee.mycoffeeassistant.data.CoffeeRepository
import com.coffee.mycoffeeassistant.ui.model.screens.BrewAssistantUiState
import com.coffee.mycoffeeassistant.ui.model.CoffeeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BrewAssistantViewModel(private val coffeeRepository: CoffeeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(BrewAssistantUiState())
    val uiState: StateFlow<BrewAssistantUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            coffeeRepository.getInStockCoffeesStream().collect { coffeeList ->
                val coffeeListSorted = coffeeList.sorted()
                val coffeeUiStateList = coffeeListSorted.map { it.toCoffeeUiState() }
                val dropdownMenuItems = coffeeListSorted.map { it.toDropdownMenuItemUiState() }

                _uiState.update {
                    it.copy(
                        coffeeUiStateList = coffeeUiStateList,
                        dropdownMenuItems = dropdownMenuItems
                    )
                }
            }
        }
    }

    fun selectCoffee(id: Int) {
//        _uiState.update { it.copy(selectedCoffee = coffeeUiState) }
    }

    private fun unselectCoffee() {
        _uiState.update { it.copy(selectedCoffee = null) }
    }

    fun onCoffeeAmountValueChange(coffeeAmount: String) {
        if (isCoffeeAmountWrongOrNegative(coffeeAmount = coffeeAmount)) {
            _uiState.update {
                it.copy(coffeeAmount = coffeeAmount, isCoffeeAmountWrong = true)
            }
        } else {
            _uiState.update {
                val waterAmount = (coffeeAmount.toFloat() * it.coffeeRatio.toFloat()).toString()
                it.copy(
                    coffeeAmount = coffeeAmount,
                    isCoffeeAmountWrong = false,
                    waterAmount = waterAmount
                )
            }
        }
    }

    fun onRatioSelected(selectedRatio: Int) {
        if (isCoffeeAmountWrongOrNegative(coffeeAmount = _uiState.value.coffeeAmount)) {
            _uiState.update {
                it.copy(coffeeRatio = selectedRatio.toString())
            }
        } else {
            _uiState.update {
                val waterAmount =
                    (it.coffeeAmount.toFloat() * selectedRatio.toFloat()).toString()
                it.copy(coffeeRatio = selectedRatio.toString(), waterAmount = waterAmount)
            }
        }
    }

    private fun isCoffeeAmountWrongOrNegative(coffeeAmount: String) =
        coffeeAmount.toFloatOrNull() == null || coffeeAmount.toFloat() < 0

    fun reduceAmount(onSuccess: () -> Unit) {
        _uiState.value.coffeeAmount.toFloatOrNull()?.let { selectedAmount ->
            _uiState.value.selectedCoffee?.let { selectedCoffee ->
                viewModelScope.launch {
                    val currentAmount = selectedCoffee.amount.toFloat()
                    val isFavourite = selectedCoffee.isFavourite
                    val isFinishingBag = currentAmount <= selectedAmount
                    if (isFinishingBag) {
                        unselectCoffee()
                        if (isFavourite) {
                            val updatedCoffee =
                                selectedCoffee.copy(amount = (-1f).toString())
                            coffeeRepository.updateCoffee(updatedCoffee.toCoffee())
                        } else {
                            coffeeRepository.deleteCoffee(selectedCoffee.toCoffee())
                        }
                    } else {
                        val updatedAmount = (currentAmount - selectedAmount).toString()
                        val updatedCoffee = selectedCoffee.copy(amount = updatedAmount)
                        coffeeRepository.updateCoffee(updatedCoffee.toCoffee())
                    }
                }
                onSuccess()
            }
        }
    }

    private fun buildDescription(coffeeUiState: CoffeeUiState): String =
        "${coffeeUiState.name}, ${coffeeUiState.brand} (${coffeeUiState.amount} g)"

}