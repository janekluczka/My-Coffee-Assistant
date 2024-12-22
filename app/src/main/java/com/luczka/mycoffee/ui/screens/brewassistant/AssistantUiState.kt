package com.luczka.mycoffee.ui.screens.brewassistant

import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.screens.brewassistant.components.DoubleVerticalPagerState
import com.luczka.mycoffee.ui.screens.brewassistant.screens.AssistantPage

sealed interface AssistantUiState {
    val pages: List<AssistantPage>
    val initialPage: Int
    val currentCoffees: List<CoffeeUiState>
    val selectedAmountsSum: String
    val waterAmount: String
    val ratioSelectionUiState: DoubleVerticalPagerState
    val isTimerRunning: Boolean
    val formattedTime: String
    val isFinished: Boolean

    data class NoneSelected(
        override val pages: List<AssistantPage> = AssistantPage.entries,
        override val initialPage: Int = 0,
        override val currentCoffees: List<CoffeeUiState> = emptyList(),
        val defaultAmountDoubleVerticalPagerState: DoubleVerticalPagerState = DoubleVerticalPagerState(
            leftPagerItems = (1..100).toList(),
            rightPagerItems = (1..9).toList(),
            leftPagerPageIndex = 0,
            rightPagerPageIndex = 0,
            separatorRes = R.string.separator_amount
        ),
        override val selectedAmountsSum: String = "0.0",
        override val ratioSelectionUiState: DoubleVerticalPagerState = DoubleVerticalPagerState(
            leftPagerItems = (1..10).toList(),
            rightPagerItems = (1..100).toList(),
            leftPagerPageIndex = 0,
            rightPagerPageIndex = 0,
            separatorRes = R.string.separator_ratio
        ),
        override val waterAmount: String = "0.0",
        override val isTimerRunning: Boolean = false,
        override val formattedTime: String = "--:--",
        override val isFinished: Boolean = false,
    ) : AssistantUiState

    data class CoffeeSelected(
        override val pages: List<AssistantPage> = AssistantPage.entries,
        override val initialPage: Int = 0,
        override val currentCoffees: List<CoffeeUiState> = emptyList(),
        val selectedCoffees: Map<CoffeeUiState, DoubleVerticalPagerState> = emptyMap(),
        override val selectedAmountsSum: String = "0.0",
        override val ratioSelectionUiState: DoubleVerticalPagerState = DoubleVerticalPagerState(
            leftPagerItems = (1..10).toList(),
            rightPagerItems = (1..100).toList(),
            leftPagerPageIndex = 0,
            rightPagerPageIndex = 0,
            separatorRes = R.string.separator_ratio
        ),
        override val waterAmount: String = "0.0",
        override val isTimerRunning: Boolean = false,
        override val formattedTime: String = "--:--",
        override val isFinished: Boolean = false
    ) : AssistantUiState
}