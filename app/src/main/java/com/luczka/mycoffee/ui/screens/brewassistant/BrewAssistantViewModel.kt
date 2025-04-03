package com.luczka.mycoffee.ui.screens.brewassistant

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.R
import com.luczka.mycoffee.domain.usecases.GetAllCoffeesUseCase
import com.luczka.mycoffee.domain.usecases.InsertBrewUseCase
import com.luczka.mycoffee.ui.components.custom.doubleverticalpager.DoubleVerticalPagerState
import com.luczka.mycoffee.ui.mappers.toModel
import com.luczka.mycoffee.ui.mappers.toUiState
import com.luczka.mycoffee.ui.models.BrewUiState
import com.luczka.mycoffee.ui.models.BrewedCoffeeUiState
import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.screens.brewassistant.state.BrewAssistantCoffeeAmountItemUiState
import com.luczka.mycoffee.ui.screens.brewassistant.state.BrewAssistantPage
import com.luczka.mycoffee.ui.screens.brewassistant.state.BrewAssistantRatioItemUiState
import com.luczka.mycoffee.ui.screens.brewassistant.state.BrewAssistantTimerItemUiState
import com.luczka.mycoffee.ui.screens.brewassistant.state.BrewAssistantUiState
import com.luczka.mycoffee.ui.util.toStringWithOneDecimalPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
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

private data class BrewAssistantViewModelState(
    val pages: List<BrewAssistantPage> = BrewAssistantPage.entries,
    val currentPage: Int = 0,
    val showAbortDialog: Boolean = false,
    val showFinishDialog: Boolean = false,
    val showBottomSheet: Boolean = false,
    val currentCoffees: List<CoffeeUiState> = emptyList(),

    val assistantRecipeCategoryUiStates: List<AssistantRecipeCategoryUiState> = emptyList(),
    val selectedRecipe: AssistantRecipeUiState? = null,

    val selectedCoffees: Map<CoffeeUiState, BrewAssistantCoffeeAmountItemUiState> = emptyMap(),
    val defaultBrewAssistantAmountUiState: BrewAssistantCoffeeAmountItemUiState = BrewAssistantCoffeeAmountItemUiState(
        openPicker = false,
        openDialog = false,
        amountDoubleVerticalPagerState = defaultAmountDoubleVerticalPagerState(),
    ),

    val brewAssistantRatioItemUiState: BrewAssistantRatioItemUiState = BrewAssistantRatioItemUiState(
        openPicker = false,
        openDialog = false,
        ratioDoubleVerticalPagerState = defaultRatioDoubleVerticalPagerState(),
    ),

    val brewAssistantTimerItemUiState: BrewAssistantTimerItemUiState = BrewAssistantTimerItemUiState(
        openPicker = false,
        openDialog = false,
        timeDoubleVerticalPagerState = defaultTimeDoubleVerticalPagerState(),
        isTimerRunning = false,
        timeInSeconds = 0,
    )
) {
    fun toBrewAssistantUiState(): BrewAssistantUiState {
        val (totalSelectedCoffeesAmount, waterAmount) = calculateAmounts()

        val selectedAmountsSumFormatted = totalSelectedCoffeesAmount.toStringWithOneDecimalPoint()
        val waterAmountFormatted = waterAmount.toStringWithOneDecimalPoint()

        return if (selectedCoffees.isEmpty()) {
            BrewAssistantUiState.NoneSelected(
                pages = pages,
                currentPage = currentPage,
                isFirstPage = currentPage == 0,
                isLastPage = currentPage == pages.lastIndex,
                showAbortDialog = showAbortDialog,
                showFinishDialog = showFinishDialog,
                showBottomSheet = showBottomSheet,
                currentCoffees = currentCoffees,
                selectedAmountsSum = selectedAmountsSumFormatted,
                waterAmount = waterAmountFormatted,

                defaultBrewAssistantAmountUiState = defaultBrewAssistantAmountUiState,
                brewAssistantRatioItemUiState = brewAssistantRatioItemUiState,
                brewAssistantTimerItemUiState = brewAssistantTimerItemUiState
            )
        } else {
            BrewAssistantUiState.CoffeeSelected(
                pages = pages,
                currentPage = currentPage,
                isFirstPage = currentPage == 0,
                isLastPage = currentPage == pages.lastIndex,
                showAbortDialog = showAbortDialog,
                showFinishDialog = showFinishDialog,
                showBottomSheet = showBottomSheet,
                currentCoffees = currentCoffees,
                selectedCoffees = selectedCoffees,
                selectedAmountsSum = selectedAmountsSumFormatted,
                waterAmount = waterAmountFormatted,

                defaultBrewAssistantAmountUiState = defaultBrewAssistantAmountUiState,
                brewAssistantRatioItemUiState = brewAssistantRatioItemUiState,
                brewAssistantTimerItemUiState = brewAssistantTimerItemUiState
            )
        }
    }

    fun toBrewUiState(): BrewUiState {
        val (totalSelectedCoffeesAmount, waterAmount) = calculateAmounts()

        val brewedCoffees = selectedCoffees.map { (coffeeUiState, brewAssistantCoffeeAmountItemUiState) ->
            BrewedCoffeeUiState(
                coffee = coffeeUiState,
                coffeeAmount = brewAssistantCoffeeAmountItemUiState.calculateCoffeeAmount(),
            )
        }

        return BrewUiState(
            addedOn = LocalDate.now(),
            coffeeAmount = totalSelectedCoffeesAmount,
            coffeeRatio = brewAssistantRatioItemUiState.coffeeRatio(),
            waterRatio = brewAssistantRatioItemUiState.waterRatio(),
            waterAmount = waterAmount,
            brewedCoffees = brewedCoffees
        )
    }

    private fun calculateAmounts(): Pair<Float, Float> {
        val totalSelectedCoffeesAmount = if (selectedCoffees.isEmpty()) {
            defaultBrewAssistantAmountUiState.calculateCoffeeAmount()
        } else {
            selectedCoffees.values
                .map { it.calculateCoffeeAmount() }
                .sum()
        }

        val selectedCoffeeRatio = brewAssistantRatioItemUiState.coffeeRatio()
        val selectedWaterRatio = brewAssistantRatioItemUiState.waterRatio()

        val waterAmount = totalSelectedCoffeesAmount * selectedWaterRatio / selectedCoffeeRatio

        return Pair(totalSelectedCoffeesAmount, waterAmount)
    }
}

@HiltViewModel
class BrewAssistantViewModel @Inject constructor(
    private val getAllCoffeesUseCase: GetAllCoffeesUseCase,
    private val insertBrewUseCase: InsertBrewUseCase
) : ViewModel() {

    private val _viewModelState = MutableStateFlow(BrewAssistantViewModelState())
    val uiState = _viewModelState
        .map(BrewAssistantViewModelState::toBrewAssistantUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = _viewModelState.value.toBrewAssistantUiState()
        )

    private val _oneTimeEvent = MutableSharedFlow<BrewAssistantOneTimeEvent>()
    val oneTimeEvent = _oneTimeEvent.asSharedFlow()

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            getAllCoffeesUseCase()
                .toUiState()
                .sortedWith(
                    compareBy<CoffeeUiState> { !it.isFavourite }
                        .thenBy { it.originOrName }
                        .thenBy { it.roasterOrBrand }
                        .thenBy { it.coffeeId }
                )
                .also { currentCoffees ->
                    _viewModelState.update { it.copy(currentCoffees = currentCoffees.toMutableList()) }
                }
        }
    }

    fun onAction(action: BrewAssistantAction) {
        when (action) {
            BrewAssistantAction.OnCloseClicked -> close()
            BrewAssistantAction.OnAbortClicked -> abort()
            BrewAssistantAction.OnPreviousClicked -> previous()
            BrewAssistantAction.OnNextClicked -> next()
            BrewAssistantAction.OnBack -> back()

            BrewAssistantAction.OnHideBottomSheet -> hideBottomSheet()
            BrewAssistantAction.OnHideAbortDialog -> hideAbortDialog()
            BrewAssistantAction.OnHideFinishDialog -> hideFinishDialog()

            is BrewAssistantAction.OnSelectRecipeClicked -> {}

            is BrewAssistantAction.OnSelectedCoffeeChanged -> selectCoffee(action.coffeeUiState)

            is BrewAssistantAction.OnAmountSelectionItemClicked -> updateAmountItemVisibility(action.coffeeUiState)
            is BrewAssistantAction.OnAmountSelectionIntegerPartIndexChanged -> updateAmountLeftPagerIndex(action.coffeeUiState, action.leftPagerPageIndex)
            is BrewAssistantAction.OnAmountSelectionFractionalPartIndexChanged -> updateAmountRightPagerIndex(action.coffeeUiState, action.rightPagerPageIndex)

            is BrewAssistantAction.OnAmountSelectionIntegerAndFractionalPartsValueChanged -> {
                if (action.key == null) {
                    updateAmountSelectionValue(leftInputValue = action.leftInputValue, rightInputValue = action.rightInputValue)
                } else {
                    updateAmountSelectionValue(key = action.key, leftInputValue = action.leftInputValue, rightInputValue = action.rightInputValue)
                }
            }

            is BrewAssistantAction.OnRatioSelectionItemClicked -> updateRatioItemVisibility()
            is BrewAssistantAction.OnRatioSelectionCoffeeIndexChanged -> updateCoffeeRatioIndex(action.leftPagerPageIndex)
            is BrewAssistantAction.OnRatioSelectionWaterIndexChanged -> updateWaterRatioIndex(action.rightPagerPageIndex)
            is BrewAssistantAction.OnRatioSelectionCoffeeAndWaterValueChanged -> updateRatioValues(action.leftInputValue, action.rightInputValue)

            BrewAssistantAction.OnTimeSelectionItemClicked -> updateTimePickerVisibility()
            BrewAssistantAction.OnResetTimerClicked -> resetTimer()
            BrewAssistantAction.OnStartStopTimerClicked -> startStopTimerTimer()
            is BrewAssistantAction.OnTimeSelectionMinutesIndexChanged -> updateTimeMinutesIndex(action.leftPagerPageIndex)
            is BrewAssistantAction.OnTimeSelectionSecondsIndexChanged -> updateTimeSecondsIndex(action.rightPagerPageIndex)

            BrewAssistantAction.OnFinishClicked -> finishBrew()
        }
    }

    private fun close() {
        if (_viewModelState.value.selectedCoffees.isEmpty()) {
            viewModelScope.launch {
                _oneTimeEvent.emit(BrewAssistantOneTimeEvent.NavigateUp)
            }
        } else {
            _viewModelState.update {
                it.copy(showAbortDialog = true)
            }
        }
    }

    private fun abort() {
        viewModelScope.launch {
            _viewModelState.update {
                it.copy(
                    showAbortDialog = false,
                    showFinishDialog = false,
                    showBottomSheet = false
                )
            }
            _oneTimeEvent.emit(BrewAssistantOneTimeEvent.NavigateUp)
        }
    }

    private fun previous() {
        _viewModelState.update { viewModelState ->
            val currentPage = viewModelState.currentPage
            viewModelState.copy(currentPage = currentPage - 1)
        }
    }

    private fun next() {
        _viewModelState.update { viewModelState ->
            val currentPage = viewModelState.currentPage
            val pages = viewModelState.pages

            if (currentPage == pages.lastIndex) {
                viewModelState.copy(showFinishDialog = true)
            } else {
                viewModelState.copy(currentPage = currentPage + 1)
            }
        }
    }

    private fun back() {
        val currentPage = _viewModelState.value.currentPage

        if (currentPage == 0) {
            close()
        } else {
            previous()
        }
    }

    private fun hideBottomSheet() {
        _viewModelState.update {
            it.copy(showBottomSheet = false)
        }
    }

    private fun hideAbortDialog() {
        _viewModelState.update {
            it.copy(showAbortDialog = false)
        }
    }

    private fun hideFinishDialog() {
        _viewModelState.update {
            it.copy(showFinishDialog = false)
        }
    }

    private fun selectCoffee(coffeeUiState: CoffeeUiState) {
        _viewModelState.update { viewModelState ->
            val updatedSelectedCoffees = viewModelState.selectedCoffees.toMutableMap()

            // TODO: Use default amount for first coffee when selecting and assign last item amount to default when removing
            if (updatedSelectedCoffees.containsKey(coffeeUiState)) {
                updatedSelectedCoffees.remove(coffeeUiState)
            } else {
                updatedSelectedCoffees[coffeeUiState] = BrewAssistantCoffeeAmountItemUiState(
                    openPicker = false,
                    openDialog = false,
                    amountDoubleVerticalPagerState = defaultAmountDoubleVerticalPagerState(),
                )
            }

            viewModelState.copy(selectedCoffees = updatedSelectedCoffees)
        }
    }

    private fun updateAmountItemVisibility(coffeeUiState: CoffeeUiState?) {
        if (coffeeUiState == null) {
            _viewModelState.update { viewModelState ->
                val defaultBrewAssistantAmountUiState = viewModelState.defaultBrewAssistantAmountUiState
                val updatedDefaultBrewAssistantAmountUiState = defaultBrewAssistantAmountUiState.copy(openPicker = !defaultBrewAssistantAmountUiState.openPicker)

                viewModelState.copy(defaultBrewAssistantAmountUiState = updatedDefaultBrewAssistantAmountUiState)
            }
        } else {

        }
    }

    private fun updateAmountLeftPagerIndex(coffeeUiState: CoffeeUiState?, leftPagerPageIndex: Int) {
        if (coffeeUiState == null) {
            _viewModelState.update { viewModelState ->
                val amountDoubleVerticalPagerState = viewModelState.defaultBrewAssistantAmountUiState.amountDoubleVerticalPagerState
                val updatedAmountSelectionUiState = amountDoubleVerticalPagerState.copy(leftPagerPageIndex = leftPagerPageIndex)
                val updatedDefaultBrewAssistantAmountUiState = viewModelState.defaultBrewAssistantAmountUiState.copy(amountDoubleVerticalPagerState = updatedAmountSelectionUiState)

                viewModelState.copy(defaultBrewAssistantAmountUiState = updatedDefaultBrewAssistantAmountUiState)
            }
        } else {
            _viewModelState.update { viewModelState ->
                val selectedCoffees = _viewModelState.value.selectedCoffees.toMutableMap()

                val amountDoubleVerticalPagerState = selectedCoffees[coffeeUiState]?.amountDoubleVerticalPagerState ?: return
                val updatedAmountSelectionUiState = amountDoubleVerticalPagerState.copy(leftPagerPageIndex = leftPagerPageIndex)
                val updatedDefaultBrewAssistantAmountUiState = viewModelState.defaultBrewAssistantAmountUiState.copy(amountDoubleVerticalPagerState = updatedAmountSelectionUiState)

                selectedCoffees.replace(coffeeUiState, updatedDefaultBrewAssistantAmountUiState)

                viewModelState.copy(selectedCoffees = selectedCoffees)
            }
        }
    }

    private fun updateAmountRightPagerIndex(coffeeUiState: CoffeeUiState?, rightPagerPageIndex: Int) {
        if (coffeeUiState == null) {
            _viewModelState.update { viewModelState ->
                val amountDoubleVerticalPagerState = viewModelState.defaultBrewAssistantAmountUiState.amountDoubleVerticalPagerState
                val updatedAmountSelectionUiState = amountDoubleVerticalPagerState.copy(rightPagerPageIndex = rightPagerPageIndex)
                val updatedDefaultBrewAssistantAmountUiState = viewModelState.defaultBrewAssistantAmountUiState.copy(amountDoubleVerticalPagerState = updatedAmountSelectionUiState)

                viewModelState.copy(defaultBrewAssistantAmountUiState = updatedDefaultBrewAssistantAmountUiState)
            }
        } else {
            _viewModelState.update { viewModelState ->
                val selectedCoffees = _viewModelState.value.selectedCoffees.toMutableMap()

                val amountDoubleVerticalPagerState = selectedCoffees[coffeeUiState]?.amountDoubleVerticalPagerState ?: return
                val updatedAmountSelectionUiState = amountDoubleVerticalPagerState.copy(rightPagerPageIndex = rightPagerPageIndex)
                val updatedDefaultBrewAssistantAmountUiState = viewModelState.defaultBrewAssistantAmountUiState.copy(amountDoubleVerticalPagerState = updatedAmountSelectionUiState)

                selectedCoffees.replace(coffeeUiState, updatedDefaultBrewAssistantAmountUiState)

                viewModelState.copy(selectedCoffees = selectedCoffees)
            }
        }
    }

    // TODO: Change to handle only 1 input value
    private fun updateAmountSelectionValue(leftInputValue: String, rightInputValue: String) {
        _viewModelState.update { viewModelState ->
            val amountDoubleVerticalPagerState = viewModelState.defaultBrewAssistantAmountUiState.amountDoubleVerticalPagerState

            val integerPart = leftInputValue.toIntOrNull() ?: return
            val fractionalPart = rightInputValue.toIntOrNull() ?: return

            val updatedAmountDoubleVerticalPagerState = amountDoubleVerticalPagerState.copy(
                leftPagerPageIndex = amountDoubleVerticalPagerState.leftPagerItems.indexOf(integerPart),
                rightPagerPageIndex = amountDoubleVerticalPagerState.rightPagerItems.indexOf(fractionalPart)
            )

            val updatedDefaultBrewAssistantAmountUiState = viewModelState.defaultBrewAssistantAmountUiState.copy(amountDoubleVerticalPagerState = updatedAmountDoubleVerticalPagerState)

            viewModelState.copy(defaultBrewAssistantAmountUiState = updatedDefaultBrewAssistantAmountUiState)
        }
    }

    // TODO: Change to handle only 1 input value
    private fun updateAmountSelectionValue(key: CoffeeUiState, leftInputValue: String, rightInputValue: String) {
        _viewModelState.update { viewModelState ->
            val selectedCoffees = viewModelState.selectedCoffees.toMutableMap()

            val amountDoubleVerticalPagerState = selectedCoffees[key]?.amountDoubleVerticalPagerState ?: return

            val integerPart = leftInputValue.toIntOrNull() ?: return
            val fractionalPart = rightInputValue.toIntOrNull() ?: return

            val updatedAmountDoubleVerticalPagerState = amountDoubleVerticalPagerState.copy(
                leftPagerPageIndex = amountDoubleVerticalPagerState.leftPagerItems.indexOf(integerPart),
                rightPagerPageIndex = amountDoubleVerticalPagerState.rightPagerItems.indexOf(fractionalPart)
            )

            val updatedDefaultBrewAssistantAmountUiState = viewModelState.defaultBrewAssistantAmountUiState.copy(amountDoubleVerticalPagerState = updatedAmountDoubleVerticalPagerState)

            selectedCoffees.replace(key, updatedDefaultBrewAssistantAmountUiState)

            viewModelState.copy(selectedCoffees = selectedCoffees)
        }
    }

    private fun updateRatioItemVisibility() {
        _viewModelState.update { viewModelState ->
            val brewAssistantTimerUiState = viewModelState.brewAssistantRatioItemUiState
            val updatedBrewAssistantRatioItemUiState = brewAssistantTimerUiState.copy(openPicker = !brewAssistantTimerUiState.openPicker)

            viewModelState.copy(brewAssistantRatioItemUiState = updatedBrewAssistantRatioItemUiState)
        }
    }

    private fun updateCoffeeRatioIndex(coffeeRatioIndex: Int) {
        _viewModelState.update { viewModelState ->
            val ratioDoubleVerticalPagerState = viewModelState.brewAssistantRatioItemUiState.ratioDoubleVerticalPagerState
            val updatedRatioSelectionUiState = ratioDoubleVerticalPagerState.copy(leftPagerPageIndex = coffeeRatioIndex)
            val updatedBrewAssistantRatioItemUiState = viewModelState.brewAssistantRatioItemUiState.copy(ratioDoubleVerticalPagerState = updatedRatioSelectionUiState)

            viewModelState.copy(brewAssistantRatioItemUiState = updatedBrewAssistantRatioItemUiState)
        }
    }

    private fun updateWaterRatioIndex(waterRatioIndex: Int) {
        _viewModelState.update { viewModelState ->
            val ratioDoubleVerticalPagerState = viewModelState.brewAssistantRatioItemUiState.ratioDoubleVerticalPagerState
            val updatedRatioSelectionUiState = ratioDoubleVerticalPagerState.copy(rightPagerPageIndex = waterRatioIndex)
            val updatedBrewAssistantRatioItemUiState = viewModelState.brewAssistantRatioItemUiState.copy(ratioDoubleVerticalPagerState = updatedRatioSelectionUiState)

            viewModelState.copy(brewAssistantRatioItemUiState = updatedBrewAssistantRatioItemUiState)
        }
    }

    private fun updateRatioValues(coffeeRatioValue: String, waterRatioValue: String) {
        _viewModelState.update { viewModelState ->
            val ratioSelectionUiState = viewModelState.brewAssistantRatioItemUiState.ratioDoubleVerticalPagerState

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

            val updatedBrewAssistantRatioItemUiState = viewModelState.brewAssistantRatioItemUiState.copy(ratioDoubleVerticalPagerState = updatedRatioSelectionUiState)

            viewModelState.copy(brewAssistantRatioItemUiState = updatedBrewAssistantRatioItemUiState)
        }
    }

    private fun updateTimePickerVisibility() {
        _viewModelState.update { viewModelState ->
            val brewAssistantTimerUiState = viewModelState.brewAssistantTimerItemUiState
            val updatedBrewAssistantTimerUiState = brewAssistantTimerUiState.copy(openPicker = !brewAssistantTimerUiState.openPicker)

            viewModelState.copy(brewAssistantTimerItemUiState = updatedBrewAssistantTimerUiState)
        }
    }

    private fun resetTimer() {
        stopTimer()
        _viewModelState.update { viewModelState ->
            val brewAssistantTimerUiState = viewModelState.brewAssistantTimerItemUiState

            val updatedTimeDoubleVerticalPagerState = brewAssistantTimerUiState.timeDoubleVerticalPagerState.copy(
                leftPagerPageIndex = 0,
                rightPagerPageIndex = 0
            )

            val updatedBrewAssistantTimerUiState = brewAssistantTimerUiState.copy(
                timeInSeconds = 0,
                timeDoubleVerticalPagerState = updatedTimeDoubleVerticalPagerState
            )

            viewModelState.copy(brewAssistantTimerItemUiState = updatedBrewAssistantTimerUiState)
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
        if (_viewModelState.value.brewAssistantTimerItemUiState.isMaxTimeReached()) return

        _viewModelState.update { viewModelState ->
            val updatedBrewAssistantTimerUiState = viewModelState.brewAssistantTimerItemUiState.copy(isTimerRunning = true)

            viewModelState.copy(brewAssistantTimerItemUiState = updatedBrewAssistantTimerUiState)
        }

        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)

                _viewModelState.update { viewModelState ->
                    val currentBrewAssistantTimerUiState = viewModelState.brewAssistantTimerItemUiState

                    if(currentBrewAssistantTimerUiState.isMaxTimeReached()) {
                        stopTimer()
                        return@update viewModelState
                    }

                    val updatedTimeInSeconds = currentBrewAssistantTimerUiState.timeInSeconds + 1

                    val updatedTimeDoubleVerticalPagerState = currentBrewAssistantTimerUiState.timeDoubleVerticalPagerState.copy(
                        leftPagerPageIndex = updatedTimeInSeconds / 60,
                        rightPagerPageIndex = updatedTimeInSeconds % 60
                    )

                    val updatedBrewAssistantTimerUiState = currentBrewAssistantTimerUiState.copy(
                        timeInSeconds = updatedTimeInSeconds,
                        timeDoubleVerticalPagerState = updatedTimeDoubleVerticalPagerState
                    )

                    viewModelState.copy(brewAssistantTimerItemUiState = updatedBrewAssistantTimerUiState)
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null

        _viewModelState.update { viewModelState ->
            val updatedBrewAssistantTimerUiState = viewModelState.brewAssistantTimerItemUiState.copy(isTimerRunning = false)

            viewModelState.copy(brewAssistantTimerItemUiState = updatedBrewAssistantTimerUiState)
        }
    }

    private fun updateTimeMinutesIndex(minutesIndex: Int) {
        _viewModelState.update { viewModelState ->
            val brewAssistantTimerUiState = viewModelState.brewAssistantTimerItemUiState

            val updatedTimeDoubleVerticalPagerState = brewAssistantTimerUiState.timeDoubleVerticalPagerState.copy(leftPagerPageIndex = minutesIndex)

            val updatedTimeInSeconds = updatedTimeDoubleVerticalPagerState.currentLeftPagerItem() * 60 + updatedTimeDoubleVerticalPagerState.currentRightPagerItem()

            val updatedBrewAssistantTimerUiState = brewAssistantTimerUiState.copy(
                timeInSeconds = updatedTimeInSeconds,
                timeDoubleVerticalPagerState = updatedTimeDoubleVerticalPagerState
            )

            viewModelState.copy(brewAssistantTimerItemUiState = updatedBrewAssistantTimerUiState)
        }
    }

    private fun updateTimeSecondsIndex(secondsIndex: Int) {
        _viewModelState.update { viewModelState ->
            val brewAssistantTimerUiState = viewModelState.brewAssistantTimerItemUiState

            val updatedTimeDoubleVerticalPagerState = brewAssistantTimerUiState.timeDoubleVerticalPagerState.copy(rightPagerPageIndex = secondsIndex)

            val updatedTimeInSeconds = updatedTimeDoubleVerticalPagerState.currentLeftPagerItem() * 60 + updatedTimeDoubleVerticalPagerState.currentRightPagerItem()

            val updatedBrewAssistantTimerUiState = brewAssistantTimerUiState.copy(
                timeInSeconds = updatedTimeInSeconds,
                timeDoubleVerticalPagerState = updatedTimeDoubleVerticalPagerState
            )

            viewModelState.copy(brewAssistantTimerItemUiState = updatedBrewAssistantTimerUiState)
        }
    }

    private fun finishBrew() {
        viewModelScope.launch {
            val brewUiState = _viewModelState.value.toBrewUiState()
            val brewId = insertBrewUseCase(brewModel = brewUiState.toModel())
            _oneTimeEvent.emit(BrewAssistantOneTimeEvent.NavigateToBrewRating(brewId = brewId))
        }
    }
}

private fun defaultAmountDoubleVerticalPagerState(): DoubleVerticalPagerState = DoubleVerticalPagerState(
    leftPagerItems = (0..199).toList(),
    rightPagerItems = (0..9).toList(),
    leftPagerPageIndex = 0,
    rightPagerPageIndex = 0,
    separatorRes = R.string.separator_amount
)

private fun defaultRatioDoubleVerticalPagerState(): DoubleVerticalPagerState = DoubleVerticalPagerState(
    leftPagerItems = (1..10).toList(),
    rightPagerItems = (1..100).toList(),
    leftPagerPageIndex = 0,
    rightPagerPageIndex = 0,
    separatorRes = R.string.separator_ratio
)

@SuppressLint("DefaultLocale")
private fun defaultTimeDoubleVerticalPagerState(): DoubleVerticalPagerState = DoubleVerticalPagerState(
    leftPagerItems = (0..59).toList(),
    rightPagerItems = (0..59).toList(),
    leftPagerPageIndex = 0,
    rightPagerPageIndex = 0,
    leftPagerItemsTextFormatter = { String.format("%02d", it) },
    rightPagerItemsTextFormatter = { String.format("%02d", it) },
    separatorRes = R.string.separator_time
)