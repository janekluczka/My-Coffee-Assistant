package com.luczka.mycoffee.ui.screens.assistant

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.R
import com.luczka.mycoffee.domain.repositories.MyCoffeeDatabaseRepository
import com.luczka.mycoffee.ui.mappers.toModel
import com.luczka.mycoffee.ui.mappers.toUiState
import com.luczka.mycoffee.ui.models.BrewUiState
import com.luczka.mycoffee.ui.models.BrewedCoffeeUiState
import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.screens.assistant.components.DoubleVerticalPagerState
import com.luczka.mycoffee.ui.util.TimeFormatter
import com.luczka.mycoffee.ui.util.toStringWithOneDecimalPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class AssistantRecipeCategoryUiState(
    @StringRes val nameRes: Int,
    val recipes: List<AssistantRecipeUiState>
)

data class AssistantRecipeUiState(
    @StringRes val nameRes: Int,
    val coffeeAmount: String,
    val coffeeRatio: Int,
    val waterRatio: Int,
    val waterAmount: String,
)

private data class AssistantViewModelState(
    val currentCoffees: List<CoffeeUiState> = emptyList(),
    val selectedCoffees: Map<CoffeeUiState, DoubleVerticalPagerState> = mapOf(),
    val assistantRecipeCategoryUiStates: List<AssistantRecipeCategoryUiState> = emptyList(),
    val selectedRecipe: AssistantRecipeUiState? = null,
    val defaultAmountDoubleVerticalPagerState: DoubleVerticalPagerState = DoubleVerticalPagerState(
        leftPagerItems = (0..100).toList(),
        rightPagerItems = (0..9).toList(),
        leftPagerPageIndex = 0,
        rightPagerPageIndex = 0,
        separatorRes = R.string.separator_amount
    ),
    val ratioDoubleVerticalPagerState: DoubleVerticalPagerState = DoubleVerticalPagerState(
        leftPagerItems = (1..10).toList(),
        rightPagerItems = (1..100).toList(),
        leftPagerPageIndex = 0,
        rightPagerPageIndex = 0,
        separatorRes = R.string.separator_ratio
    ),
    val isTimerRunning: Boolean = false,
    val timeInSeconds: Int? = null,
    val isFinished: Boolean = false,
) {
    fun toAssistantUiState(): AssistantUiState {
        val selectedAmountsSum = sumSelectedAmounts()

        val selectedCoffeeRatio = ratioDoubleVerticalPagerState.currentLeftPagerItem()
        val selectedWaterRatio = ratioDoubleVerticalPagerState.currentRightPagerItem()

        val waterAmount = selectedAmountsSum * selectedWaterRatio / selectedCoffeeRatio

        val selectedAmountsSumFormatted = selectedAmountsSum.toStringWithOneDecimalPoint()
        val waterAmountFormatted = waterAmount.toStringWithOneDecimalPoint()

        return if (selectedCoffees.isEmpty()) {
            AssistantUiState.NoneSelected(
                currentCoffees = currentCoffees,
                isFinished = isFinished,
                selectedAmountsSum = selectedAmountsSumFormatted,
                waterAmount = waterAmountFormatted,
                defaultAmountDoubleVerticalPagerState = defaultAmountDoubleVerticalPagerState,
                ratioSelectionUiState = ratioDoubleVerticalPagerState,
                isTimerRunning = isTimerRunning,
                formattedTime = TimeFormatter.formatTime(timeInSeconds)
            )
        } else {
            AssistantUiState.CoffeeSelected(
                currentCoffees = currentCoffees,
                selectedCoffees = selectedCoffees,
                isFinished = isFinished,
                selectedAmountsSum = selectedAmountsSumFormatted,
                waterAmount = waterAmountFormatted,
                ratioSelectionUiState = ratioDoubleVerticalPagerState,
                isTimerRunning = isTimerRunning,
                formattedTime = TimeFormatter.formatTime(timeInSeconds)
            )
        }
    }

    fun toBrewModel(): BrewUiState {
        val selectedAmountsSum = sumSelectedAmounts()

        val selectedCoffeeRatio = ratioDoubleVerticalPagerState.currentLeftPagerItem()
        val selectedWaterRatio = ratioDoubleVerticalPagerState.currentRightPagerItem()

        val waterAmount = selectedAmountsSum * selectedWaterRatio / selectedCoffeeRatio

        val brewedCoffees = selectedCoffees.map { (coffeeUiState, amountDoubleVerticalPagerState) ->
            val integerPart = amountDoubleVerticalPagerState.currentLeftPagerItem()
            val fractionalPart = amountDoubleVerticalPagerState.currentRightPagerItem()
            val coffeeAmount = "$integerPart.$fractionalPart".toFloatOrNull() ?: 0f
            BrewedCoffeeUiState(
                coffeeAmount = coffeeAmount,
                coffee = coffeeUiState,
            )
        }

        return BrewUiState(
            addedOn = LocalDate.now(),
            coffeeAmount = selectedAmountsSum,
            coffeeRatio = selectedCoffeeRatio,
            waterAmount = waterAmount,
            waterRatio = selectedWaterRatio,
            brewedCoffees = brewedCoffees
        )
    }

    private fun sumSelectedAmounts(): Float {
        return if (selectedCoffees.isEmpty()) {
            val integerPart = defaultAmountDoubleVerticalPagerState.currentLeftPagerItem()
            val fractionalPart = defaultAmountDoubleVerticalPagerState.currentRightPagerItem()
            "$integerPart.$fractionalPart".toFloatOrNull() ?: 0f
        } else {
            selectedCoffees.values
                .map { amountDoubleVerticalPagerState ->
                    val integerPart = amountDoubleVerticalPagerState.currentLeftPagerItem()
                    val fractionalPart = amountDoubleVerticalPagerState.currentRightPagerItem()
                    "$integerPart.$fractionalPart".toFloatOrNull() ?: 0f
                }
                .sum()
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

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            myCoffeeDatabaseRepository.getCurrentCoffeesStream().collect { coffeeModels ->
                val coffeeUiStateList = coffeeModels
                    .map { it.toUiState() }
                    .sortedWith(
                        compareBy<CoffeeUiState> { !it.isFavourite }
                            .thenBy { it.originOrName }
                            .thenBy { it.roasterOrBrand }
                            .thenBy { it.amount }
                    )
                viewModelState.update { it.copy(currentCoffees = coffeeUiStateList) }
            }
        }
    }

    fun onAction(action: AssistantAction) {
        when (action) {
            is AssistantAction.NavigateUp -> {}
            is AssistantAction.NavigateToAssistantRating -> {}

            is AssistantAction.OnShowAbortDialog -> {}
            is AssistantAction.OnSelectRecipeClicked -> {}
            is AssistantAction.OnShowFinishDialog -> {}

            is AssistantAction.OnSelectedCoffeeChanged -> selectCoffee(action.coffeeUiState)
            is AssistantAction.OnAmountSelectionIntegerPartIndexChanged -> {
                if (action.key == null) {
                    updateAmountLeftPagerIndex(leftPagerPageIndex = action.leftPagerPageIndex)
                } else {
                    updateAmountLeftPagerIndex(key = action.key, leftPagerPageIndex = action.leftPagerPageIndex)
                }
            }

            is AssistantAction.OnAmountSelectionFractionalPartIndexChanged -> {
                if (action.key == null) {
                    updateAmountRightPagerIndex(rightPagerPageIndex = action.rightPagerPageIndex)
                } else {
                    updateAmountRightPagerIndex(key = action.key, rightPagerPageIndex = action.rightPagerPageIndex)
                }
            }

            is AssistantAction.OnAmountSelectionIntegerAndFractionalPartsValueChanged -> {
                if (action.key == null) {
                    updateAmountSelectionValue(action.leftInputValue, action.rightInputValue)
                } else {
                    updateAmountSelectionValue(action.key, action.leftInputValue, action.rightInputValue)
                }
            }

            is AssistantAction.OnRatioSelectionCoffeeIndexChanged -> updateCoffeeRatioIndex(action.leftPagerPageIndex)
            is AssistantAction.OnRatioSelectionWaterIndexChanged -> updateWaterRatioIndex(action.rightPagerPageIndex)
            is AssistantAction.OnRatioSelectionCoffeeAndWaterValueChanged -> updateRatioValues(action.leftInputValue, action.rightInputValue)
            is AssistantAction.OnResetTimerClicked -> resetTimer()
            is AssistantAction.OnStartStopTimerClicked -> startStopTimerTimer()
//            is AssistantAction.OnFinishButtonClicked -> finishBrew()
            is AssistantAction.OnFinishButtonClicked -> tempFinishBrew()

        }
    }

    private fun selectCoffee(coffeeUiState: CoffeeUiState) {
        val selectedCoffeeAmount = coffeeUiState.amount.toFloatOrNull()?.toInt()

        val maxLeftPagerIndex = when {
            selectedCoffeeAmount == null -> 0
            selectedCoffeeAmount < 100 -> selectedCoffeeAmount
            else -> 100
        }

        val updatedSelectedCoffees = viewModelState.value.selectedCoffees.toMutableMap()

        if (updatedSelectedCoffees.containsKey(coffeeUiState)) {
            updatedSelectedCoffees.remove(coffeeUiState)
        } else {
            updatedSelectedCoffees[coffeeUiState] = DoubleVerticalPagerState(
                leftPagerItems = (0..maxLeftPagerIndex).toList(),
                rightPagerItems = (0..9).toList(),
                leftPagerPageIndex = 0,
                rightPagerPageIndex = 0,
                separatorRes = R.string.separator_amount
            )
        }

        viewModelState.update {
            it.copy(selectedCoffees = updatedSelectedCoffees)
        }
    }

    private fun updateAmountLeftPagerIndex(leftPagerPageIndex: Int) {
        val amountSelectionUiState = viewModelState.value.defaultAmountDoubleVerticalPagerState
        val updatedAmountSelectionUiState = amountSelectionUiState.copy(leftPagerPageIndex = leftPagerPageIndex)
        viewModelState.update {
            it.copy(defaultAmountDoubleVerticalPagerState = updatedAmountSelectionUiState)
        }
    }

    private fun updateAmountRightPagerIndex(rightPagerPageIndex: Int) {
        val amountSelectionUiState = viewModelState.value.defaultAmountDoubleVerticalPagerState
        val updatedAmountSelectionUiState = amountSelectionUiState.copy(rightPagerPageIndex = rightPagerPageIndex)
        viewModelState.update {
            it.copy(defaultAmountDoubleVerticalPagerState = updatedAmountSelectionUiState)
        }
    }

    private fun updateAmountLeftPagerIndex(key: CoffeeUiState, leftPagerPageIndex: Int) {
        val selectedCoffees = viewModelState.value.selectedCoffees.toMutableMap()

        val amountDoubleVerticalPagerState = selectedCoffees[key] ?: return

        val integerPart = amountDoubleVerticalPagerState.currentLeftPagerItem()
        val fractionalPart = amountDoubleVerticalPagerState.currentRightPagerItem()

        val maxAmount = key.amount
        val selectedAmount = "$integerPart.$fractionalPart"

        val maxAmountFloat = maxAmount.toFloatOrNull() ?: return
        val selectedAmountFloat = selectedAmount.toFloatOrNull() ?: return

        val updatedAmountSelectionUiState = if (selectedAmountFloat > maxAmountFloat) {
            val selectedAmountFractionalPart = selectedAmount.split(".")[1].toIntOrNull() ?: return
            val maxAmountFractionalPart = maxAmount.split(".")[1].toIntOrNull() ?: return

            val fractionalPartIndexOffset = selectedAmountFractionalPart - maxAmountFractionalPart

            val adjustedFractionalPartIndex = amountDoubleVerticalPagerState.leftPagerPageIndex - fractionalPartIndexOffset

            amountDoubleVerticalPagerState.copy(
                leftPagerPageIndex = leftPagerPageIndex,
                rightPagerPageIndex = adjustedFractionalPartIndex,
            )
        } else {
            amountDoubleVerticalPagerState.copy(leftPagerPageIndex = leftPagerPageIndex)
        }

        selectedCoffees.replace(key, updatedAmountSelectionUiState)

        viewModelState.update { it.copy(selectedCoffees = selectedCoffees) }
    }

    private fun updateAmountRightPagerIndex(key: CoffeeUiState, rightPagerPageIndex: Int) {
        val selectedCoffees = viewModelState.value.selectedCoffees.toMutableMap()

        val amountSelectionUiState = selectedCoffees[key] ?: return

        val updatedAmountSelectionUiState = amountSelectionUiState.copy(rightPagerPageIndex = rightPagerPageIndex)

        selectedCoffees.replace(key, updatedAmountSelectionUiState)

        viewModelState.update { it.copy(selectedCoffees = selectedCoffees) }
    }

    private fun updateAmountSelectionValue(leftInputValue: String, rightInputValue: String) {
        val amountSelectionUiState = viewModelState.value.defaultAmountDoubleVerticalPagerState

        val integerPart = leftInputValue.toIntOrNull() ?: return
        val fractionalPart = rightInputValue.toIntOrNull() ?: return

        val updatedAmountSelectionUiState = amountSelectionUiState.copy(
            leftPagerPageIndex = amountSelectionUiState.leftPagerItems.indexOf(integerPart),
            rightPagerPageIndex = amountSelectionUiState.rightPagerItems.indexOf(fractionalPart)
        )

        viewModelState.update { it.copy(defaultAmountDoubleVerticalPagerState = updatedAmountSelectionUiState) }
    }

    private fun updateAmountSelectionValue(key: CoffeeUiState, leftInputValue: String, rightInputValue: String) {
        val selectedCoffees = viewModelState.value.selectedCoffees.toMutableMap()

        val amountSelectionUiState = selectedCoffees[key] ?: return

        val integerPart = leftInputValue.toIntOrNull() ?: return
        val fractionalPart = rightInputValue.toIntOrNull() ?: return

        val maxAmount = key.amount
        val selectedAmount = "$integerPart.$fractionalPart"

        val maxAmountFloat = maxAmount.toFloatOrNull() ?: return
        val selectedAmountFloat = selectedAmount.toFloatOrNull() ?: return

        val updatedAmountSelectionUiState = if (selectedAmountFloat > maxAmountFloat) {
            val selectedAmountFractionalPart = selectedAmount.split(".")[1].toIntOrNull() ?: return
            val maxAmountFractionalPart = maxAmount.split(".")[1].toIntOrNull() ?: return

            val fractionalPartIndexOffset = selectedAmountFractionalPart - maxAmountFractionalPart

            val adjustedFractionalPartIndex = amountSelectionUiState.leftPagerPageIndex - fractionalPartIndexOffset

            amountSelectionUiState.copy(
                leftPagerPageIndex = amountSelectionUiState.leftPagerItems.indexOf(integerPart),
                rightPagerPageIndex = adjustedFractionalPartIndex,
            )
        } else {
            amountSelectionUiState.copy(
                leftPagerPageIndex = amountSelectionUiState.leftPagerItems.indexOf(integerPart),
                rightPagerPageIndex = amountSelectionUiState.rightPagerItems.indexOf(fractionalPart)
            )
        }

        selectedCoffees.replace(key, updatedAmountSelectionUiState)

        viewModelState.update { it.copy(selectedCoffees = selectedCoffees) }
    }

    private fun updateCoffeeRatioIndex(coffeeRatioIndex: Int) {
        val ratioSelectionUiState = viewModelState.value.ratioDoubleVerticalPagerState

        val updatedRatioSelectionUiState = ratioSelectionUiState.copy(leftPagerPageIndex = coffeeRatioIndex)

        viewModelState.update { it.copy(ratioDoubleVerticalPagerState = updatedRatioSelectionUiState) }
    }

    private fun updateWaterRatioIndex(waterRatioIndex: Int) {
        val ratioSelectionUiState = viewModelState.value.ratioDoubleVerticalPagerState

        val updatedRatioSelectionUiState = ratioSelectionUiState.copy(rightPagerPageIndex = waterRatioIndex)

        viewModelState.update { it.copy(ratioDoubleVerticalPagerState = updatedRatioSelectionUiState) }
    }

    private fun updateRatioValues(coffeeRatioValue: String, waterRatioValue: String) {
        val ratioSelectionUiState = viewModelState.value.ratioDoubleVerticalPagerState

        val selectedCoffeeRatio = if (coffeeRatioValue.isNotBlank()) {
            coffeeRatioValue.toIntOrNull() ?: return
        } else {
            ratioSelectionUiState.currentLeftPagerItem()
        }

        val selectedWaterRatio = if (waterRatioValue.isNotBlank()) {
            waterRatioValue.toIntOrNull() ?: return
        } else {
            ratioSelectionUiState.currentRightPagerItem()
        }

        val updatedRatioSelectionUiState = ratioSelectionUiState.copy(
            leftPagerPageIndex = ratioSelectionUiState.leftPagerItems.indexOf(selectedCoffeeRatio),
            rightPagerPageIndex = ratioSelectionUiState.rightPagerItems.indexOf(selectedWaterRatio)
        )

        viewModelState.update { it.copy(ratioDoubleVerticalPagerState = updatedRatioSelectionUiState) }
    }

    private fun resetTimer() {
        stopTimer()
        viewModelState.update { state ->
            state.copy(timeInSeconds = null)
        }
    }

    private fun startStopTimerTimer() {
        if (timerJob?.isActive == true) {
            stopTimer()
        } else {

            startTimer()
        }
    }

    private fun startTimer() {
        viewModelState.update {
            it.copy(isTimerRunning = true)
        }

        timerJob = viewModelScope.launch {
            while (true) {
                if (viewModelState.value.timeInSeconds == null) {
                    viewModelState.update {
                        it.copy(timeInSeconds = 0)
                    }
                }
                delay(1000)
                viewModelState.update {
                    it.copy(timeInSeconds = (it.timeInSeconds ?: 0) + 1)
                }
            }
        }
    }

    private fun stopTimer() {
        viewModelState.update {
            it.copy(isTimerRunning = false)
        }
        timerJob?.cancel()
        timerJob = null
    }


    private fun tempFinishBrew() {
        viewModelState.update { it.copy(isFinished = true) }
    }


    private fun finishBrew() {
        viewModelScope.launch {
            val brew = viewModelState.value.toBrewModel()

            myCoffeeDatabaseRepository.insertBrew(brew.toModel())

            viewModelState.value.selectedCoffees.forEach { (selectedCoffee, amountDoubleVerticalPagerState) ->
                val selectedCoffeeAmount = selectedCoffee.amount.toFloatOrNull() ?: 0f

                val integerPart = amountDoubleVerticalPagerState.currentLeftPagerItem()
                val fractionalPart = amountDoubleVerticalPagerState.currentRightPagerItem()

                val selectedAmount = "$integerPart.$fractionalPart".toFloatOrNull() ?: 0f

                val updatedAmount = selectedCoffeeAmount - selectedAmount

                val updatedCoffee = if (updatedAmount <= 0) {
                    selectedCoffee.copy(amount = 0.0f.toString())
                } else {
                    selectedCoffee.copy(amount = updatedAmount.toString())
                }

                myCoffeeDatabaseRepository.updateCoffeeOld(updatedCoffee.toModel())
            }

            viewModelState.update { it.copy(isFinished = true) }
        }
    }

}