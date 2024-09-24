package com.luczka.mycoffee.ui.screens.assistant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.data.database.entities.BrewEntity
import com.luczka.mycoffee.data.database.entities.BrewedCoffeeEntity
import com.luczka.mycoffee.domain.repository.MyCoffeeDatabaseRepository
import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.util.toStringWithOneDecimalPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class AmountSelectionUiState(
    val integerParts: List<Int> = (0..0).toList(),
    val decimalParts: List<Int> = (0..9).toList(),
    val integerPartIndex: Int = 0,
    val decimalPartIndex: Int = 0,
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
    // TODO: Change to nullable key MutableMap<CoffeeUiState?, AmountSelectionUiState>
    val selectedCoffees: MutableMap<CoffeeUiState, AmountSelectionUiState> = mutableMapOf(),
    val amountSelectionUiState: AmountSelectionUiState = AmountSelectionUiState(integerParts = (0..99).toList()),
    val ratioSelectionUiState: RatioSelectionUiState = RatioSelectionUiState(),
    val rating: Int? = null,
    val notes: String = "",
    val isFinished: Boolean = false,
) {
    fun toAssistantUiState(): AssistantUiState {
        val selectedAmountsSum = sumSelectedAmounts()

        val selectedWaterRatio = ratioSelectionUiState.selectedWaterRatio
        val selectedCoffeeRatio = ratioSelectionUiState.selectedCoffeeRatio

        val waterAmount = selectedAmountsSum * selectedWaterRatio / selectedCoffeeRatio

        val selectedAmountsSumFormatted = selectedAmountsSum.toStringWithOneDecimalPoint()
        val waterAmountFormatted = waterAmount.toStringWithOneDecimalPoint()

        return if (selectedCoffees.isEmpty()) {
            AssistantUiState.NoneSelected(
                currentCoffees = currentCoffees,
                isFinished = isFinished,
                selectedAmountsSum = selectedAmountsSumFormatted,
                waterAmount = waterAmountFormatted,
                amountSelectionUiState = amountSelectionUiState,
                ratioSelectionUiState = ratioSelectionUiState,
                rating = rating,
                notes = notes
            )
        } else {
            AssistantUiState.CoffeeSelected(
                currentCoffees = currentCoffees,
                selectedCoffees = selectedCoffees,
                isFinished = isFinished,
                selectedAmountsSum = selectedAmountsSumFormatted,
                waterAmount = waterAmountFormatted,
                ratioSelectionUiState = ratioSelectionUiState,
                rating = rating,
                notes = notes
            )
        }
    }

    fun toBrew(): BrewEntity {
        val coffeeAmountsSum = sumSelectedAmounts()

        val coffeeRatio = ratioSelectionUiState.selectedCoffeeRatio
        val waterRatio = ratioSelectionUiState.selectedWaterRatio

        val waterAmount = coffeeAmountsSum * waterRatio / coffeeRatio

        return BrewEntity(
            brewId = 0,
            date = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE),
            coffeeAmount = coffeeAmountsSum,
            coffeeRatio = coffeeRatio,
            waterAmount = waterAmount,
            waterRatio = waterRatio,
            rating = rating,
            notes = notes
        )
    }

    private fun sumSelectedAmounts(): Float {
        return if (selectedCoffees.isEmpty()) {
            amountSelectionUiState.selectedAmount.toFloatOrNull() ?: 0f
        } else {
            selectedCoffees.values.map { it.selectedAmount.toFloatOrNull() ?: 0f }.sum()
        }
    }
}

@HiltViewModel
class AssistantViewModel @Inject constructor(
    private val myCoffeeDatabaseRepository: MyCoffeeDatabaseRepository
) : ViewModel() {

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
            myCoffeeDatabaseRepository.getCurrentCoffeesStream().collect { coffeeList ->
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

    fun onAction(action: AssistantAction) {
        when (action) {
            AssistantAction.NavigateUp -> {}
            is AssistantAction.OnSelectedCoffeeChanged -> selectCoffee(action.coffeeUiState)
            is AssistantAction.OnCoffeeAmountSelectionIntegerPartIndexChanged -> {
                if (action.key == null) {
                    updateAmountSelectionIntegerPart(action.integerPartIndex)
                } else {
                    updateAmountSelectionIntegerPart(action.key, action.integerPartIndex)
                }
            }

            is AssistantAction.OnCoffeeAmountSelectionDecimalPartIndexChanged -> {
                if (action.key == null) {
                    updateAmountSelectionDecimalPart(action.decimalPartIndex)
                } else {
                    updateAmountSelectionDecimalPart(action.key, action.decimalPartIndex)
                }
            }

            is AssistantAction.OnCoffeeAmountSelectionValueChanged -> {
                if (action.key == null) {
                    updateAmountSelectionValue(action.amountSelectionValue)
                } else {
                    updateAmountSelectionValue(action.key, action.amountSelectionValue)
                }
            }

            is AssistantAction.OnCoffeeRatioIndexChanged -> updateCoffeeRatioIndex(action.coffeeRatioIndex)
            is AssistantAction.OnWaterRatioIndexChanged -> updateWaterRatioIndex(action.waterRatioIndex)
            is AssistantAction.OnRatioValueChanged -> updateRatioValues(action.coffeeRatioValue, action.waterRatioValue)
            is AssistantAction.OnNotesChanged -> updateNotes(action.notes)
            is AssistantAction.OnRatingChanged -> updateRating(action.rating)
            AssistantAction.OnFinishBrew -> finishBrew()
        }
    }

    private fun selectCoffee(coffeeUiState: CoffeeUiState) {
        val selectedCoffeeAmount = coffeeUiState.amount?.toFloatOrNull()?.toInt()

        val lastIntegerPart = when {
            selectedCoffeeAmount == null -> 0
            selectedCoffeeAmount > 99 -> 99
            else -> selectedCoffeeAmount
        }

        val updatedSelectedCoffees = viewModelState.value.selectedCoffees.toMutableMap()

        if (updatedSelectedCoffees.containsKey(coffeeUiState)) {
            updatedSelectedCoffees.remove(coffeeUiState)
        } else {
            updatedSelectedCoffees[coffeeUiState] = AmountSelectionUiState(
                integerParts = (0..lastIntegerPart).toList()
            )
        }

        viewModelState.update { it.copy(selectedCoffees = updatedSelectedCoffees) }
    }

    private fun updateAmountSelectionIntegerPart(integerPartIndex: Int) {
        val amountSelectionUiState = viewModelState.value.amountSelectionUiState

        val decimalPartIndex = amountSelectionUiState.decimalPartIndex

        val integerPart = amountSelectionUiState.integerParts[integerPartIndex]
        val decimalPart = amountSelectionUiState.decimalParts[decimalPartIndex]

        val updatedAmountSelectionUiState = amountSelectionUiState.copy(
            integerPartIndex = integerPartIndex,
            selectedAmount = "$integerPart.$decimalPart"
        )

        viewModelState.update { it.copy(amountSelectionUiState = updatedAmountSelectionUiState) }
    }

    private fun updateAmountSelectionDecimalPart(decimalPartIndex: Int) {
        val amountSelectionUiState = viewModelState.value.amountSelectionUiState

        val integerPartIndex = amountSelectionUiState.integerPartIndex

        val integerPart = amountSelectionUiState.integerParts[integerPartIndex]
        val decimalPart = amountSelectionUiState.decimalParts[decimalPartIndex]

        val updatedAmountSelectionUiState = amountSelectionUiState.copy(
            decimalPartIndex = decimalPartIndex,
            selectedAmount = "$integerPart.$decimalPart"
        )

        viewModelState.update { it.copy(amountSelectionUiState = updatedAmountSelectionUiState) }
    }

    private fun updateAmountSelectionIntegerPart(key: CoffeeUiState, integerPartIndex: Int) {
        val selectedCoffees = viewModelState.value.selectedCoffees.toMutableMap()

        val amountSelectionUiState = selectedCoffees[key] ?: return

        val decimalPartIndex = amountSelectionUiState.decimalPartIndex

        val integerPart = amountSelectionUiState.integerParts[integerPartIndex]
        val decimalPart = amountSelectionUiState.decimalParts[decimalPartIndex]

        val maxAmount = key.amount ?: return
        val selectedAmount = "$integerPart.$decimalPart"

        val maxAmountFloat = maxAmount.toFloatOrNull() ?: return
        val selectedAmountFloat = selectedAmount.toFloatOrNull() ?: return

        val updatedAmountSelectionUiState = if (selectedAmountFloat > maxAmountFloat) {
            val selectedAmountDecimalPart = selectedAmount.split(".")[1].toIntOrNull() ?: return
            val maxAmountDecimalPart = maxAmount.split(".")[1].toIntOrNull() ?: return

            val decimalPartIndexOffset = selectedAmountDecimalPart - maxAmountDecimalPart

            val updatedDecimalPartIndex = decimalPartIndex - decimalPartIndexOffset

            val updatedDecimalPart = amountSelectionUiState.decimalParts[updatedDecimalPartIndex]

            amountSelectionUiState.copy(
                integerPartIndex = integerPartIndex,
                decimalPartIndex = updatedDecimalPartIndex,
                selectedAmount = "$integerPart.$updatedDecimalPart",
            )
        } else {
            amountSelectionUiState.copy(
                integerPartIndex = integerPartIndex,
                selectedAmount = "$integerPart.$decimalPart"
            )
        }

        selectedCoffees.replace(key, updatedAmountSelectionUiState)

        viewModelState.update { it.copy(selectedCoffees = selectedCoffees) }
    }

    private fun updateAmountSelectionDecimalPart(key: CoffeeUiState, decimalPartIndex: Int) {
        val selectedCoffees = viewModelState.value.selectedCoffees.toMutableMap()

        val amountSelectionUiState = selectedCoffees[key] ?: return

        val integerPartIndex = amountSelectionUiState.integerPartIndex

        val integerPart = amountSelectionUiState.integerParts[integerPartIndex]
        val decimalPart = amountSelectionUiState.decimalParts[decimalPartIndex]

        val updatedAmountSelectionUiState = amountSelectionUiState.copy(
            decimalPartIndex = decimalPartIndex,
            selectedAmount = "$integerPart.$decimalPart"
        )

        selectedCoffees.replace(key, updatedAmountSelectionUiState)

        viewModelState.update { it.copy(selectedCoffees = selectedCoffees) }
    }

    private fun updateAmountSelectionValue(coffeeAmountValue: String) {
        val amountSelectionUiState = viewModelState.value.amountSelectionUiState

        val coffeeAmountFloat = coffeeAmountValue.toFloatOrNull() ?: return

        val coffeeAmountValueWithDecimalPoint = coffeeAmountFloat.toStringWithOneDecimalPoint()

        val coffeeAmountSplit = coffeeAmountValueWithDecimalPoint.split(".")

        val integerPart = coffeeAmountSplit[0].toIntOrNull() ?: return
        val decimalPart = coffeeAmountSplit[1].toIntOrNull() ?: return

        val updatedAmountSelectionUiState = amountSelectionUiState.copy(
            selectedAmount = coffeeAmountValue,
            integerPartIndex = amountSelectionUiState.integerParts.indexOf(integerPart),
            decimalPartIndex = amountSelectionUiState.decimalParts.indexOf(decimalPart)
        )

        viewModelState.update { it.copy(amountSelectionUiState = updatedAmountSelectionUiState) }
    }

    private fun updateAmountSelectionValue(key: CoffeeUiState, coffeeAmountValue: String) {
        val selectedCoffees = viewModelState.value.selectedCoffees

        val amountSelectionUiState = selectedCoffees[key] ?: return

        val coffeeAmountFloat = coffeeAmountValue.toFloatOrNull() ?: return

        val coffeeAmountValueWithDecimalPoint = coffeeAmountFloat.toStringWithOneDecimalPoint()

        val coffeeAmountSplit = coffeeAmountValueWithDecimalPoint.split(".")

        val integerPart = coffeeAmountSplit[0].toIntOrNull() ?: return
        val decimalPart = coffeeAmountSplit[1].toIntOrNull() ?: return

        val updatedAmountSelectionUiState = amountSelectionUiState.copy(
            selectedAmount = coffeeAmountValue,
            integerPartIndex = amountSelectionUiState.integerParts.indexOf(integerPart),
            decimalPartIndex = amountSelectionUiState.decimalParts.indexOf(decimalPart)
        )

        selectedCoffees.replace(key, updatedAmountSelectionUiState)

        viewModelState.update { it.copy(selectedCoffees = selectedCoffees) }
    }

    private fun updateCoffeeRatioIndex(coffeeRatioIndex: Int) {
        val ratioSelectionUiState = viewModelState.value.ratioSelectionUiState

        val updatedRatioSelectionUiState = ratioSelectionUiState.copy(
            coffeeRatioIndex = coffeeRatioIndex,
            selectedCoffeeRatio = ratioSelectionUiState.coffeeRatios[coffeeRatioIndex],
        )

        viewModelState.update { it.copy(ratioSelectionUiState = updatedRatioSelectionUiState) }
    }

    private fun updateWaterRatioIndex(waterRatioIndex: Int) {
        val ratioSelectionUiState = viewModelState.value.ratioSelectionUiState

        val updatedRatioSelectionUiState = ratioSelectionUiState.copy(
            waterRatioIndex = waterRatioIndex,
            selectedWaterRatio = ratioSelectionUiState.waterRatios[waterRatioIndex],
        )

        viewModelState.update { it.copy(ratioSelectionUiState = updatedRatioSelectionUiState) }
    }

    private fun updateRatioValues(coffeeRatioValue: String, waterRatioValue: String) {
        val ratioSelectionUiState = viewModelState.value.ratioSelectionUiState

        val selectedCoffeeRatio = if (coffeeRatioValue.isNotBlank()) {
            coffeeRatioValue.toIntOrNull() ?: return
        } else {
            ratioSelectionUiState.selectedCoffeeRatio
        }

        val selectedWaterRatio = if (waterRatioValue.isNotBlank()) {
            waterRatioValue.toIntOrNull() ?: return
        } else {
            ratioSelectionUiState.selectedWaterRatio
        }

        val updatedRatioSelectionUiState = ratioSelectionUiState.copy(
            selectedCoffeeRatio = selectedCoffeeRatio,
            selectedWaterRatio = selectedWaterRatio,
            coffeeRatioIndex = ratioSelectionUiState.coffeeRatios.indexOf(selectedCoffeeRatio),
            waterRatioIndex = ratioSelectionUiState.waterRatios.indexOf(selectedWaterRatio)
        )

        viewModelState.update { it.copy(ratioSelectionUiState = updatedRatioSelectionUiState) }
    }

    private fun updateRating(rating: Int?) {
        viewModelState.update { it.copy(rating = rating) }
    }

    private fun updateNotes(notes: String) {
        viewModelState.update { it.copy(notes = notes) }
    }

    private fun finishBrew() {
        viewModelScope.launch {
            val brewId = insertBrew()
            updateSelectedCoffees(brewId = brewId)
            viewModelState.update { it.copy(isFinished = true) }
        }
    }

    private suspend fun insertBrew(): Long {
        val brew = viewModelState.value.toBrew()
        return myCoffeeDatabaseRepository.insertBrew(brewEntity = brew)
    }

    private suspend fun updateSelectedCoffees(brewId: Long) {
        viewModelState.value.selectedCoffees.forEach { (selectedCoffee, amountSelectionUiState) ->
            val selectedCoffeeAmount = selectedCoffee.amount?.toFloatOrNull() ?: 0f

            val selectedAmount = amountSelectionUiState.selectedAmount.toFloatOrNull() ?: 0f

            val updatedAmount = selectedCoffeeAmount - selectedAmount

            val updatedCoffee = if (updatedAmount <= 0) {
                selectedCoffee.copy(amount = null)
            } else {
                selectedCoffee.copy(amount = updatedAmount.toString())
            }

            val brewedCoffeeEntity = BrewedCoffeeEntity(
                brewId = brewId.toInt(),
                coffeeId = selectedCoffee.coffeeId,
                coffeeAmount = selectedAmount
            )

            myCoffeeDatabaseRepository.insertBrewedCoffee(brewedCoffeeEntity)

            myCoffeeDatabaseRepository.updateCoffee(updatedCoffee.toCoffee())
        }
    }

}