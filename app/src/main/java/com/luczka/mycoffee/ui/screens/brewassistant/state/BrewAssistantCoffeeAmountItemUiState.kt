package com.luczka.mycoffee.ui.screens.brewassistant.state

import com.luczka.mycoffee.ui.components.custom.doubleverticalpager.DoubleVerticalPagerState

data class BrewAssistantCoffeeAmountItemUiState(
    val openPicker: Boolean,
    val openDialog: Boolean,
    val amountDoubleVerticalPagerState: DoubleVerticalPagerState,
) {
    fun calculateCoffeeAmount(): Float {
        val integerPart = amountDoubleVerticalPagerState.currentLeftPagerItem()
        val fractionalPart = amountDoubleVerticalPagerState.currentRightPagerItem()
        return "$integerPart.$fractionalPart".toFloatOrNull() ?: 0f
    }
}