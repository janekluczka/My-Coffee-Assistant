package com.luczka.mycoffee.ui.screens.brewassistant

import com.luczka.mycoffee.ui.models.CoffeeUiState

sealed class AssistantAction {
    data object NavigateUp : AssistantAction()
    data class NavigateToAssistantRating(val brewId: Long) : AssistantAction()

    data object OnShowAbortDialog : AssistantAction()
    data object OnShowFinishDialog : AssistantAction()

    data class OnSelectedCoffeeChanged(val coffeeUiState: CoffeeUiState) : AssistantAction()
    data object OnSelectRecipeClicked : AssistantAction()
    data class OnAmountSelectionIntegerPartIndexChanged(
        val key: CoffeeUiState?,
        val leftPagerPageIndex: Int
    ) : AssistantAction()
    data class OnAmountSelectionFractionalPartIndexChanged(
        val key: CoffeeUiState?,
        val rightPagerPageIndex: Int
    ) : AssistantAction()
    data class OnAmountSelectionIntegerAndFractionalPartsValueChanged(
        val key: CoffeeUiState?,
        val leftInputValue: String,
        val rightInputValue: String
    ) : AssistantAction()
    data class OnRatioSelectionCoffeeIndexChanged(val leftPagerPageIndex: Int) : AssistantAction()
    data class OnRatioSelectionWaterIndexChanged(val rightPagerPageIndex: Int) : AssistantAction()
    data class OnRatioSelectionCoffeeAndWaterValueChanged(
        val leftInputValue: String,
        val rightInputValue: String
    ) : AssistantAction()
    data object OnResetTimerClicked: AssistantAction()
    data object OnStartStopTimerClicked: AssistantAction()
    data object OnFinishButtonClicked : AssistantAction()
}