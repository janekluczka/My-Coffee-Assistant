package com.luczka.mycoffee.ui.screens.brewassistant

import com.luczka.mycoffee.R
import com.luczka.mycoffee.ui.components.custom.doubleverticalpager.DoubleVerticalPagerState
import com.luczka.mycoffee.ui.models.CoffeeUiState

sealed interface BrewAssistantUiState {
    val pages: List<BrewAssistantPage>
    val currentPage: Int
    val isFirstPage: Boolean
    val isLastPage: Boolean
    val showAbortDialog: Boolean
    val showFinishDialog: Boolean
    val showBottomSheet: Boolean
    val currentCoffees: List<CoffeeUiState>
    val recipeCategories: List<AssistantRecipeCategoryUiState>
    val selectedAmountsSum: String
    val waterAmount: String
    val ratioSelectionUiState: DoubleVerticalPagerState
    val isTimerRunning: Boolean
    val formattedTime: String

    data class NoneSelected(
        override val pages: List<BrewAssistantPage> = BrewAssistantPage.entries,
        override val currentPage: Int = 0,
        override val isFirstPage: Boolean = true,
        override val isLastPage: Boolean = false,
        override val showAbortDialog: Boolean = false,
        override val showFinishDialog: Boolean = false,
        override val showBottomSheet: Boolean = false,
        override val currentCoffees: List<CoffeeUiState> = emptyList(),
        override val recipeCategories: List<AssistantRecipeCategoryUiState> = emptyList(),
        val defaultAmountDoubleVerticalPagerState: DoubleVerticalPagerState = DoubleVerticalPagerState(
            leftPagerItems = (1..100).toList(),
            rightPagerItems = (1..9).toList(), //
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
        override val formattedTime: String = "--:--"
    ) : BrewAssistantUiState

    data class CoffeeSelected(
        override val pages: List<BrewAssistantPage> = BrewAssistantPage.entries,
        override val currentPage: Int = 0,
        override val isFirstPage: Boolean = true,
        override val isLastPage: Boolean = false,
        override val showAbortDialog: Boolean = false,
        override val showFinishDialog: Boolean = false,
        override val showBottomSheet: Boolean = false,
        override val currentCoffees: List<CoffeeUiState> = emptyList(),
        override val recipeCategories: List<AssistantRecipeCategoryUiState> = emptyList(),
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
        override val formattedTime: String = "--:--"
    ) : BrewAssistantUiState
}