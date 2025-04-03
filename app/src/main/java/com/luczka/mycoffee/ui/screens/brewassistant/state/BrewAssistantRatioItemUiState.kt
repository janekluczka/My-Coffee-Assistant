package com.luczka.mycoffee.ui.screens.brewassistant.state

import com.luczka.mycoffee.ui.components.custom.doubleverticalpager.DoubleVerticalPagerState

data class BrewAssistantRatioItemUiState(
    val openPicker: Boolean,
    val openDialog: Boolean,
    val ratioDoubleVerticalPagerState: DoubleVerticalPagerState,
) {
    fun coffeeRatio(): Int {
        return ratioDoubleVerticalPagerState.currentLeftPagerItem()
    }

    fun waterRatio(): Int {
        return ratioDoubleVerticalPagerState.currentRightPagerItem()
    }
}