package com.luczka.mycoffee.ui.screens.brewassistant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.data.CoffeeRepository
import com.luczka.mycoffee.ui.model.CoffeeUiState
import com.luczka.mycoffee.util.toStringWithOneDecimalPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BrewAssistantUiState(
    val currentCoffees: List<CoffeeUiState> = emptyList(),
    val selectedCoffees: Map<CoffeeUiState, AmountSelectionUiState> = emptyMap(),
    val moreThanOneCoffeeSelected: Boolean = false,
    val hasAmountValue: Boolean = false,
    val hasRatioValue: Boolean = false,
    val selectedAmountsSum: String = "0.0",
    val waterAmount: String = "0.0",
    val ratioSelectionUiState: RatioSelectionUiState = RatioSelectionUiState(),
    val isFinished: Boolean = false
)

data class AmountSelectionUiState(
    val openPicker: Boolean = false,
    val openDialog: Boolean = false,
    val wholeNumbers: List<Int> = (0..0).toList(),
    val fractionalParts: List<Int> = (0..9).toList(),
    val wholeNumberIndex: Int = 0,
    val fractionalPartIndex: Int = 0,
    val selectedAmount: String = "0.0"
)

data class RatioSelectionUiState(
    val coffeeRatios: List<Int> = (1..10).toList(),
    val waterRatios: List<Int> = (1..100).toList(),
    val coffeeRatioIndex: Int = 0,
    val waterRatioIndex: Int = 0,
    val selectedCoffeeRatio: Int = 1,
    val selectedWaterRatio: Int = 1
)

private data class AssistantViewModelState(
    val currentCoffees: List<CoffeeUiState> = emptyList(),
    val selectedCoffees: MutableMap<CoffeeUiState, AmountSelectionUiState> = mutableMapOf(),
    val hasRatioValue: Boolean = false,
    val isFinished: Boolean = false,
    val ratioSelectionUiState: RatioSelectionUiState = RatioSelectionUiState(),
) {
    fun toAssistantUiState(): BrewAssistantUiState {
        val selectedAmountsSum = selectedCoffees.values
            .map { it.selectedAmount.toFloatOrNull() ?: 0f }
            .sum()

        val selectedWaterRatio = ratioSelectionUiState.selectedWaterRatio
        val selectedCoffeeRatio = ratioSelectionUiState.selectedCoffeeRatio

        val waterAmount = selectedAmountsSum * selectedWaterRatio / selectedCoffeeRatio

        val selectedAmountsSumFormatted = selectedAmountsSum.toStringWithOneDecimalPoint()
        val waterAmountFormatted = waterAmount.toStringWithOneDecimalPoint()

        return BrewAssistantUiState(
            currentCoffees = currentCoffees,
            selectedCoffees = selectedCoffees,
            moreThanOneCoffeeSelected = selectedCoffees.size > 1,
            hasAmountValue = true,
            hasRatioValue = hasRatioValue,
            isFinished = isFinished,
            selectedAmountsSum = selectedAmountsSumFormatted,
            waterAmount = waterAmountFormatted,
            ratioSelectionUiState = ratioSelectionUiState
        )
    }
}

class AssistantViewModel(private val coffeeRepository: CoffeeRepository) : ViewModel() {

    private val viewModelState = MutableStateFlow(AssistantViewModelState())
    val uiState = viewModelState
        .map(AssistantViewModelState::toAssistantUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = viewModelState.value.toAssistantUiState()
        )

    init {
        viewModelScope.launch {
            coffeeRepository.getInStockCoffeesStream().collect { coffeeList ->
                val coffeeUiStateList = coffeeList
                    .map { it.toCoffeeUiState() }
                    .sortedWith(
                        compareBy<CoffeeUiState> { !it.isFavourite }
                            .thenBy { it.name }
                            .thenBy { it.brand }
                            .thenBy { it.amount }
                    )
                viewModelState.update { it.copy(currentCoffees = coffeeUiStateList) }
            }
        }
    }

    fun selectCoffee(coffeeUiState: CoffeeUiState) {
        val selectedCoffeeAmount = coffeeUiState.amount?.toFloatOrNull()?.toInt()

        val lastWholeNumber = when {
            selectedCoffeeAmount == null -> 0
            selectedCoffeeAmount > 99 -> 99
            else -> selectedCoffeeAmount
        }

        val wholeNumbers = (0..lastWholeNumber).toList()

        val updatedSelectedCoffees = viewModelState.value.selectedCoffees.toMutableMap()

        if (updatedSelectedCoffees.containsKey(coffeeUiState)) {
            updatedSelectedCoffees.remove(coffeeUiState)
        } else {
            updatedSelectedCoffees[coffeeUiState] = AmountSelectionUiState(
                wholeNumbers = wholeNumbers
            )
        }

        viewModelState.update { it.copy(selectedCoffees = updatedSelectedCoffees) }
    }

    fun updateAmountSelectionWholeNumber(
        key: CoffeeUiState,
        wholeNumberIndex: Int
    ) {
        val selectedCoffees = viewModelState.value.selectedCoffees.toMutableMap()

        val amountSelectionUiState = selectedCoffees[key] ?: return

        val wholeNumber =
            amountSelectionUiState.wholeNumbers[wholeNumberIndex]
        val fractionalPart =
            amountSelectionUiState.fractionalParts[amountSelectionUiState.fractionalPartIndex]

        val selectedAmount = "$wholeNumber.$fractionalPart"

        val updatedAmountSelectionUiState = amountSelectionUiState.copy(
            wholeNumberIndex = wholeNumberIndex,
            selectedAmount = selectedAmount
        )

        selectedCoffees.replace(key, updatedAmountSelectionUiState)

        viewModelState.update { it.copy(selectedCoffees = selectedCoffees) }
    }

    fun updateAmountSelectionFractionalPart(
        key: CoffeeUiState,
        fractionalPartIndex: Int
    ) {
        val selectedCoffees = viewModelState.value.selectedCoffees.toMutableMap()

        val amountSelectionUiState = selectedCoffees[key] ?: return

        val wholeNumber =
            amountSelectionUiState.wholeNumbers[amountSelectionUiState.wholeNumberIndex]
        val fractionalPart =
            amountSelectionUiState.fractionalParts[fractionalPartIndex]

        val selectedAmount = "$wholeNumber.$fractionalPart"

        val updatedAmountSelectionUiState = amountSelectionUiState.copy(
            fractionalPartIndex = fractionalPartIndex,
            selectedAmount = selectedAmount
        )

        selectedCoffees.replace(key, updatedAmountSelectionUiState)

        viewModelState.update { it.copy(selectedCoffees = selectedCoffees) }
    }

    fun updateAmountSelectionText(
        key: CoffeeUiState,
        coffeeAmountText: String
    ) {
        val selectedCoffees = viewModelState.value.selectedCoffees

        val amountSelectionUiState = selectedCoffees[key] ?: return

        val coffeeAmountFloat = coffeeAmountText.toFloatOrNull() ?: return

        val selectedAmount = coffeeAmountFloat.toStringWithOneDecimalPoint()

        val updatedAmountSelectionUiState = amountSelectionUiState.copy(
            selectedAmount = selectedAmount
        )

        selectedCoffees.replace(key, updatedAmountSelectionUiState)

        // TODO: Add finding indexes

        viewModelState.update { it.copy(selectedCoffees = selectedCoffees) }
    }

    fun updateHasRatio() {
        viewModelState.update { it.copy(hasRatioValue = true) }
    }

    fun updateCoffeeRatioIndex(coffeeRatioIndex: Int) {
        updateRatios(coffeeRatioIndex = coffeeRatioIndex)
    }

    fun updateWaterRatioIndex(waterRatioIndex: Int) {
        updateRatios(waterRatioIndex = waterRatioIndex)
    }

    private fun updateRatios(
        coffeeRatioIndex: Int = viewModelState.value.ratioSelectionUiState.coffeeRatioIndex,
        waterRatioIndex: Int = viewModelState.value.ratioSelectionUiState.waterRatioIndex
    ) {
        val selectedCoffeeRatio =
            viewModelState.value.ratioSelectionUiState.coffeeRatios[coffeeRatioIndex]
        val selectedWaterRatio =
            viewModelState.value.ratioSelectionUiState.waterRatios[waterRatioIndex]

        viewModelState.update {
            it.copy(
                ratioSelectionUiState = it.ratioSelectionUiState.copy(
                    coffeeRatioIndex = coffeeRatioIndex,
                    waterRatioIndex = waterRatioIndex,
                    selectedCoffeeRatio = selectedCoffeeRatio,
                    selectedWaterRatio = selectedWaterRatio,
                )
            )
        }
    }

    fun updateRatioText(coffeeRatioText: String, waterAmountText: String) {
        val selectedCoffeeRatio = coffeeRatioText.toIntOrNull() ?: return
        val selectedWaterRatio = waterAmountText.toIntOrNull() ?: return

        // TODO: Add finding indexes

        viewModelState.update {
            it.copy(
                ratioSelectionUiState = it.ratioSelectionUiState.copy(
                    selectedCoffeeRatio = selectedCoffeeRatio,
                    selectedWaterRatio = selectedWaterRatio
                )
            )
        }
    }

    fun finishBrew() {
        val selectedCoffees = viewModelState.value.selectedCoffees

        viewModelScope.launch {
            selectedCoffees.forEach { (selectedCoffee, amountSelectionUiState) ->
                val selectedCoffeeAmount = selectedCoffee.amount?.toFloatOrNull() ?: 0f
                val selectedAmount = amountSelectionUiState.selectedAmount.toFloatOrNull() ?: 0f

                val updatedAmount = selectedCoffeeAmount - selectedAmount

                val isFinishingBag = updatedAmount <= 0

                val updatedCoffee = if (isFinishingBag) {
                    selectedCoffee.copy(amount = null)
                } else {
                    selectedCoffee.copy(amount = updatedAmount.toString())
                }

                coffeeRepository.updateCoffee(updatedCoffee.toCoffee())
            }

            viewModelState.update { it.copy(isFinished = true) }
        }
    }

}