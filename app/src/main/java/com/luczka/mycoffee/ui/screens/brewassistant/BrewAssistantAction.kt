package com.luczka.mycoffee.ui.screens.brewassistant

import com.luczka.mycoffee.ui.models.CoffeeUiState

sealed class BrewAssistantAction {
    data object OnAbort : BrewAssistantAction()
    data object OnBack : BrewAssistantAction()
    data object OnPrevious : BrewAssistantAction()
    data object OnNext : BrewAssistantAction()

    data object OnHideAbortDialog : BrewAssistantAction()
    data object OnHideFinishDialog : BrewAssistantAction()
    data object OnHideBottomSheet : BrewAssistantAction()

    data class OnSelectedCoffeeChanged(val coffeeUiState: CoffeeUiState) : BrewAssistantAction()
    data object OnSelectRecipeClicked : BrewAssistantAction()
    data class OnAmountSelectionIntegerPartIndexChanged(
        val key: CoffeeUiState?,
        val leftPagerPageIndex: Int
    ) : BrewAssistantAction()

    data class OnAmountSelectionFractionalPartIndexChanged(
        val key: CoffeeUiState?,
        val rightPagerPageIndex: Int
    ) : BrewAssistantAction()

    data class OnAmountSelectionIntegerAndFractionalPartsValueChanged(
        val key: CoffeeUiState?,
        val leftInputValue: String,
        val rightInputValue: String
    ) : BrewAssistantAction()

    data class OnRatioSelectionCoffeeIndexChanged(val leftPagerPageIndex: Int) : BrewAssistantAction()
    data class OnRatioSelectionWaterIndexChanged(val rightPagerPageIndex: Int) : BrewAssistantAction()
    data class OnRatioSelectionCoffeeAndWaterValueChanged(
        val leftInputValue: String,
        val rightInputValue: String
    ) : BrewAssistantAction()

    data object OnResetTimerClicked : BrewAssistantAction()
    data object OnStartStopTimerClicked : BrewAssistantAction()
    data object OnFinishBrewClicked : BrewAssistantAction()
}