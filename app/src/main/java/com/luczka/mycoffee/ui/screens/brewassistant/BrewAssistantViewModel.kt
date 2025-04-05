package com.luczka.mycoffee.ui.screens.brewassistant

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luczka.mycoffee.domain.usecases.GetAllCoffeesUseCase
import com.luczka.mycoffee.domain.usecases.InsertBrewUseCase
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
    val defaultBrewAssistantAmountUiState: BrewAssistantCoffeeAmountItemUiState = BrewAssistantCoffeeAmountItemUiState(),
    val brewAssistantRatioItemUiState: BrewAssistantRatioItemUiState = BrewAssistantRatioItemUiState(),
    val brewAssistantTimerItemUiState: BrewAssistantTimerItemUiState = BrewAssistantTimerItemUiState()
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
            coffeeRatio = brewAssistantRatioItemUiState.selectedCoffeeRatio(),
            waterRatio = brewAssistantRatioItemUiState.selectedWaterRatio(),
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

        val selectedCoffeeRatio = brewAssistantRatioItemUiState.selectedCoffeeRatio()
        val selectedWaterRatio = brewAssistantRatioItemUiState.selectedWaterRatio()

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
                    updateAmountSelectionValue(coffeeUiState = action.key, leftInputValue = action.leftInputValue, rightInputValue = action.rightInputValue)
                }
            }

            is BrewAssistantAction.OnRatioSelectionItemClicked -> updateRatioItemVisibility()
            is BrewAssistantAction.OnRatioSelectionCoffeeIndexChanged -> updateCoffeeRatioIndex(action.leftPagerPageIndex)
            is BrewAssistantAction.OnRatioSelectionWaterIndexChanged -> updateWaterRatioIndex(action.rightPagerPageIndex)
            is BrewAssistantAction.OnRatioSelectionCoffeeAndWaterValueChanged -> updateRatioValues(action.leftInputValue, action.rightInputValue)

            BrewAssistantAction.OnTimeSelectionItemClicked -> updateTimePickerVisibility()
            BrewAssistantAction.OnResetTimerClicked -> resetTimer()
            BrewAssistantAction.OnStartStopTimerClicked -> startStopTimerTimer()
            is BrewAssistantAction.OnTimeSelectionMinutesIndexChanged -> updateTimeMinutesPageIndex(action.leftPagerPageIndex)
            is BrewAssistantAction.OnTimeSelectionSecondsIndexChanged -> updateTimeSecondsPageIndex(action.rightPagerPageIndex)

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
                updatedSelectedCoffees[coffeeUiState] = BrewAssistantCoffeeAmountItemUiState()
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
                val updatedDefaultBrewAssistantAmountUiState = viewModelState.defaultBrewAssistantAmountUiState.copy(leftPagerPageIndex = leftPagerPageIndex)
                viewModelState.copy(defaultBrewAssistantAmountUiState = updatedDefaultBrewAssistantAmountUiState)
            }
        } else {
            _viewModelState.update { viewModelState ->
                val selectedCoffees = _viewModelState.value.selectedCoffees.toMutableMap()

                val brewAssistantCoffeeAmountItemUiState = selectedCoffees[coffeeUiState] ?: return
                val updatedBrewAssistantCoffeeAmountItemUiState = brewAssistantCoffeeAmountItemUiState.copy(leftPagerPageIndex = leftPagerPageIndex)

                selectedCoffees.replace(coffeeUiState, updatedBrewAssistantCoffeeAmountItemUiState)

                viewModelState.copy(selectedCoffees = selectedCoffees)
            }
        }
    }

    private fun updateAmountRightPagerIndex(coffeeUiState: CoffeeUiState?, rightPagerPageIndex: Int) {
        if (coffeeUiState == null) {
            _viewModelState.update { viewModelState ->
                val updatedDefaultBrewAssistantAmountUiState = viewModelState.defaultBrewAssistantAmountUiState.copy(rightPagerPageIndex = rightPagerPageIndex)
                viewModelState.copy(defaultBrewAssistantAmountUiState = updatedDefaultBrewAssistantAmountUiState)
            }
        } else {
            _viewModelState.update { viewModelState ->
                val selectedCoffees = _viewModelState.value.selectedCoffees.toMutableMap()

                val brewAssistantCoffeeAmountItemUiState = selectedCoffees[coffeeUiState] ?: return
                val updatedBrewAssistantCoffeeAmountItemUiState = brewAssistantCoffeeAmountItemUiState.copy(rightPagerPageIndex = rightPagerPageIndex)

                selectedCoffees.replace(coffeeUiState, updatedBrewAssistantCoffeeAmountItemUiState)

                viewModelState.copy(selectedCoffees = selectedCoffees)
            }
        }
    }

    // TODO: Change to handle only 1 input value
    private fun updateAmountSelectionValue(leftInputValue: String, rightInputValue: String) {
        _viewModelState.update { viewModelState ->
            val defaultBrewAssistantAmountUiState = viewModelState.defaultBrewAssistantAmountUiState

            val integerPart = leftInputValue.toIntOrNull() ?: return
            val fractionalPart = rightInputValue.toIntOrNull() ?: return

            val updatedDefaultBrewAssistantAmountUiState = defaultBrewAssistantAmountUiState.copy(
                leftPagerPageIndex = defaultBrewAssistantAmountUiState.leftPagerItems.indexOf(integerPart),
                rightPagerPageIndex = defaultBrewAssistantAmountUiState.rightPagerItems.indexOf(fractionalPart)
            )

            viewModelState.copy(defaultBrewAssistantAmountUiState = updatedDefaultBrewAssistantAmountUiState)
        }
    }

    // TODO: Change to handle only 1 input value
    private fun updateAmountSelectionValue(coffeeUiState: CoffeeUiState, leftInputValue: String, rightInputValue: String) {
        _viewModelState.update { viewModelState ->
            val selectedCoffees = viewModelState.selectedCoffees.toMutableMap()

            val brewAssistantCoffeeAmountItemUiState = selectedCoffees[coffeeUiState] ?: return

            val integerPart = leftInputValue.toIntOrNull() ?: return
            val fractionalPart = rightInputValue.toIntOrNull() ?: return

            val updatedBrewAssistantCoffeeAmountItemUiState = brewAssistantCoffeeAmountItemUiState.copy(
                leftPagerPageIndex = brewAssistantCoffeeAmountItemUiState.leftPagerItems.indexOf(integerPart),
                rightPagerPageIndex = brewAssistantCoffeeAmountItemUiState.rightPagerItems.indexOf(fractionalPart)
            )

            selectedCoffees.replace(coffeeUiState, updatedBrewAssistantCoffeeAmountItemUiState)

            viewModelState.copy(selectedCoffees = selectedCoffees)
        }
    }

    private fun updateRatioItemVisibility() {
        _viewModelState.update { viewModelState ->
            val brewAssistantRatioItemUiState = viewModelState.brewAssistantRatioItemUiState
            val updatedBrewAssistantRatioItemUiState = brewAssistantRatioItemUiState.copy(openPicker = !brewAssistantRatioItemUiState.openPicker)
            viewModelState.copy(brewAssistantRatioItemUiState = updatedBrewAssistantRatioItemUiState)
        }
    }

    private fun updateCoffeeRatioIndex(coffeeRatioIndex: Int) {
        _viewModelState.update { viewModelState ->
            val updatedBrewAssistantRatioItemUiState = viewModelState.brewAssistantRatioItemUiState.copy(coffeeRatioIndex = coffeeRatioIndex)
            viewModelState.copy(brewAssistantRatioItemUiState = updatedBrewAssistantRatioItemUiState)
        }
    }

    private fun updateWaterRatioIndex(waterRatioIndex: Int) {
        _viewModelState.update { viewModelState ->
            val updatedBrewAssistantRatioItemUiState = viewModelState.brewAssistantRatioItemUiState.copy(waterRatioIndex = waterRatioIndex)
            viewModelState.copy(brewAssistantRatioItemUiState = updatedBrewAssistantRatioItemUiState)
        }
    }

    private fun updateRatioValues(coffeeRatioValue: String, waterRatioValue: String) {
        _viewModelState.update { viewModelState ->
            val brewAssistantRatioItemUiState = viewModelState.brewAssistantRatioItemUiState

            val selectedCoffeeRatio = if (coffeeRatioValue.isNotBlank()) {
                coffeeRatioValue.toIntOrNull() ?: return
            } else {
                brewAssistantRatioItemUiState.selectedCoffeeRatio()
            }

            val selectedWaterRatio = if (waterRatioValue.isNotBlank()) {
                waterRatioValue.toIntOrNull() ?: return
            } else {
                brewAssistantRatioItemUiState.selectedWaterRatio()
            }

            val updatedBrewAssistantRatioItemUiState = brewAssistantRatioItemUiState.copy(
                coffeeRatioIndex = brewAssistantRatioItemUiState.coffeeRatioItems.indexOf(selectedCoffeeRatio),
                waterRatioIndex = brewAssistantRatioItemUiState.waterRatioItems.indexOf(selectedWaterRatio)
            )

            viewModelState.copy(brewAssistantRatioItemUiState = updatedBrewAssistantRatioItemUiState)
        }
    }

    private fun updateTimePickerVisibility() {
        _viewModelState.update { viewModelState ->
            val brewAssistantTimerItemUiState = viewModelState.brewAssistantTimerItemUiState
            val updatedBrewAssistantTimerItemUiState = brewAssistantTimerItemUiState.copy(openPicker = !brewAssistantTimerItemUiState.openPicker)
            viewModelState.copy(brewAssistantTimerItemUiState = updatedBrewAssistantTimerItemUiState)
        }
    }

    private fun resetTimer() {
        stopTimer()
        _viewModelState.update { viewModelState ->
            val updatedBrewAssistantTimerItemUiState = viewModelState.brewAssistantTimerItemUiState.copy(minutesPageIndex = 0, secondsPageIndex = 0)
            viewModelState.copy(brewAssistantTimerItemUiState = updatedBrewAssistantTimerItemUiState)
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
            val updatedBrewAssistantTimerItemUiState = viewModelState.brewAssistantTimerItemUiState.copy(isRunning = true)
            viewModelState.copy(brewAssistantTimerItemUiState = updatedBrewAssistantTimerItemUiState)
        }

        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)

                _viewModelState.update { viewModelState ->
                    val brewAssistantTimerUiState = viewModelState.brewAssistantTimerItemUiState

                    if(brewAssistantTimerUiState.isMaxTimeReached()) {
                        stopTimer()
                        return@update viewModelState
                    }

                    val updatedTimeInSeconds = brewAssistantTimerUiState.timeInSeconds + 1

                    val updatedBrewAssistantTimerItemUiState = brewAssistantTimerUiState.copy(
                        timeInSeconds = updatedTimeInSeconds,
                        minutesPageIndex = updatedTimeInSeconds / 60,
                        secondsPageIndex = updatedTimeInSeconds % 60
                    )

                    viewModelState.copy(brewAssistantTimerItemUiState = updatedBrewAssistantTimerItemUiState)
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null

        _viewModelState.update { viewModelState ->
            val updatedBrewAssistantTimerItemUiState = viewModelState.brewAssistantTimerItemUiState.copy(isRunning = false)
            viewModelState.copy(brewAssistantTimerItemUiState = updatedBrewAssistantTimerItemUiState)
        }
    }

    private fun updateTimeMinutesPageIndex(minutesPageIndex: Int) {
        _viewModelState.update { viewModelState ->
            val brewAssistantTimerItemUiState = viewModelState.brewAssistantTimerItemUiState

            val updatedTimeInSeconds = brewAssistantTimerItemUiState.minutesAt(minutesPageIndex) * 60 + brewAssistantTimerItemUiState.selectedSeconds()

            val updatedBrewAssistantTimerItemUiState = brewAssistantTimerItemUiState.copy(
                minutesPageIndex = minutesPageIndex,
                timeInSeconds = updatedTimeInSeconds
            )

            viewModelState.copy(brewAssistantTimerItemUiState = updatedBrewAssistantTimerItemUiState)
        }
    }

    private fun updateTimeSecondsPageIndex(secondsPageIndex: Int) {
        _viewModelState.update { viewModelState ->
            val brewAssistantTimerUiState = viewModelState.brewAssistantTimerItemUiState

            val updatedTimeInSeconds = brewAssistantTimerUiState.selectedMinutes() * 60 + brewAssistantTimerUiState.secondsAt(secondsPageIndex)

            val updatedBrewAssistantTimerItemUiState = brewAssistantTimerUiState.copy(
                secondsPageIndex = secondsPageIndex,
                timeInSeconds = updatedTimeInSeconds
            )

            viewModelState.copy(brewAssistantTimerItemUiState = updatedBrewAssistantTimerItemUiState)
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