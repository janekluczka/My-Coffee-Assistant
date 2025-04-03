package com.luczka.mycoffee.ui.screens.brewassistant

import com.luczka.mycoffee.ui.models.CoffeeUiState

sealed class BrewAssistantAction {
    data object OnCloseClicked : BrewAssistantAction()
    data object OnAbortClicked : BrewAssistantAction()
    data object OnPreviousClicked : BrewAssistantAction()
    data object OnNextClicked : BrewAssistantAction()
    data object OnBack : BrewAssistantAction()

    data object OnHideAbortDialog : BrewAssistantAction()
    data object OnHideFinishDialog : BrewAssistantAction()
    data object OnHideBottomSheet : BrewAssistantAction()

    data class OnSelectedCoffeeChanged(val coffeeUiState: CoffeeUiState) : BrewAssistantAction()
    data object OnSelectRecipeClicked : BrewAssistantAction()

    data class OnAmountSelectionItemClicked(val coffeeUiState: CoffeeUiState?) : BrewAssistantAction()
    data class OnAmountSelectionIntegerPartIndexChanged(val coffeeUiState: CoffeeUiState?, val leftPagerPageIndex: Int) : BrewAssistantAction()
    data class OnAmountSelectionFractionalPartIndexChanged(val coffeeUiState: CoffeeUiState?, val rightPagerPageIndex: Int) : BrewAssistantAction()

    data class OnAmountSelectionIntegerAndFractionalPartsValueChanged(
        val key: CoffeeUiState?,
        val leftInputValue: String,
        val rightInputValue: String
    ) : BrewAssistantAction()

    data object OnRatioSelectionItemClicked : BrewAssistantAction()
    data class OnRatioSelectionCoffeeIndexChanged(val leftPagerPageIndex: Int) : BrewAssistantAction()
    data class OnRatioSelectionWaterIndexChanged(val rightPagerPageIndex: Int) : BrewAssistantAction()

    data class OnRatioSelectionCoffeeAndWaterValueChanged(
        val leftInputValue: String,
        val rightInputValue: String
    ) : BrewAssistantAction()

    data object OnTimeSelectionItemClicked : BrewAssistantAction()
    data object OnStartStopTimerClicked : BrewAssistantAction()
    data object OnResetTimerClicked : BrewAssistantAction()
    data class OnTimeSelectionMinutesIndexChanged(val leftPagerPageIndex: Int) : BrewAssistantAction()
    data class OnTimeSelectionSecondsIndexChanged(val rightPagerPageIndex: Int) : BrewAssistantAction()

    data object OnFinishClicked : BrewAssistantAction()
}