package com.luczka.mycoffee.ui.screens.brewassistant.state

import com.luczka.mycoffee.ui.models.CoffeeUiState
import com.luczka.mycoffee.ui.screens.brewassistant.AssistantRecipeCategoryUiState

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

    val defaultBrewAssistantAmountUiState: BrewAssistantCoffeeAmountItemUiState
    val brewAssistantRatioItemUiState: BrewAssistantRatioItemUiState
    val brewAssistantTimerItemUiState: BrewAssistantTimerItemUiState

    data class NoneSelected(
        override val pages: List<BrewAssistantPage> = BrewAssistantPage.entries,
        override val currentPage: Int,
        override val isFirstPage: Boolean,
        override val isLastPage: Boolean,
        override val showAbortDialog: Boolean,
        override val showFinishDialog: Boolean,
        override val showBottomSheet: Boolean,
        override val currentCoffees: List<CoffeeUiState>,

        override val recipeCategories: List<AssistantRecipeCategoryUiState> = emptyList(),

        override val selectedAmountsSum: String = "0.0",
        override val waterAmount: String = "0.0",

        override val defaultBrewAssistantAmountUiState: BrewAssistantCoffeeAmountItemUiState,
        override val brewAssistantRatioItemUiState: BrewAssistantRatioItemUiState,
        override val brewAssistantTimerItemUiState: BrewAssistantTimerItemUiState
    ) : BrewAssistantUiState

    data class CoffeeSelected(
        override val pages: List<BrewAssistantPage> = BrewAssistantPage.entries,
        override val currentPage: Int,
        override val isFirstPage: Boolean,
        override val isLastPage: Boolean,
        override val showAbortDialog: Boolean,
        override val showFinishDialog: Boolean,
        override val showBottomSheet: Boolean,
        override val currentCoffees: List<CoffeeUiState>,

        override val recipeCategories: List<AssistantRecipeCategoryUiState> = emptyList(),
        val selectedCoffees: Map<CoffeeUiState, BrewAssistantCoffeeAmountItemUiState> = emptyMap(),

        override val selectedAmountsSum: String = "0.0",
        override val waterAmount: String = "0.0",

        override val defaultBrewAssistantAmountUiState: BrewAssistantCoffeeAmountItemUiState,
        override val brewAssistantRatioItemUiState: BrewAssistantRatioItemUiState,
        override val brewAssistantTimerItemUiState: BrewAssistantTimerItemUiState
    ) : BrewAssistantUiState
}