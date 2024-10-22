package com.luczka.mycoffee.ui.screens.assistant

import com.luczka.mycoffee.ui.models.CoffeeUiState

sealed class AssistantAction {
    data object NavigateUp : AssistantAction()
    data class OnSelectedCoffeeChanged(val coffeeUiState: CoffeeUiState) : AssistantAction()
    data object OnSelectRecipeClicked : AssistantAction()
    data class OnCoffeeAmountSelectionIntegerPartIndexChanged(val key: CoffeeUiState?, val integerPartIndex: Int) : AssistantAction()
    data class OnCoffeeAmountSelectionDecimalPartIndexChanged(val key: CoffeeUiState?, val decimalPartIndex: Int) : AssistantAction()
    data class OnCoffeeAmountSelectionValueChanged(val key: CoffeeUiState?, val amountSelectionValue: String) : AssistantAction()
    data class OnCoffeeRatioIndexChanged(val coffeeRatioIndex: Int) : AssistantAction()
    data class OnWaterRatioIndexChanged(val waterRatioIndex: Int) : AssistantAction()
    data class OnRatioValueChanged(val coffeeRatioValue: String, val waterRatioValue: String) : AssistantAction()
    data class OnRatingChanged(val rating: Int?) : AssistantAction()
    data class OnNotesChanged(val notes: String) : AssistantAction()
    data object OnFinishBrew : AssistantAction()
}