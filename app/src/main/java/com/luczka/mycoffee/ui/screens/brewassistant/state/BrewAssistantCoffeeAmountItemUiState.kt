package com.luczka.mycoffee.ui.screens.brewassistant.state

import androidx.annotation.StringRes
import com.luczka.mycoffee.R

data class BrewAssistantCoffeeAmountItemUiState(
    val openPicker: Boolean = false,
    val openDialog: Boolean = false,
    val leftPagerPageIndex: Int = 0,
    val leftPagerItems: List<Int> = (0..249).toList(),
    val leftPagerItemsTextFormatter: ((Int) -> String)? = null,
    val rightPagerPageIndex: Int = 0,
    val rightPagerItems: List<Int> = (0..9).toList(),
    val rightPagerItemsTextFormatter: ((Int) -> String)? = null,
    @StringRes val separatorRes: Int = R.string.separator_amount,
) {
    fun selectedIntegerPart(): Int {
        return leftPagerItems[leftPagerPageIndex]
    }

    fun selectedFractionalPart(): Int {
        return rightPagerItems[rightPagerPageIndex]
    }

    fun calculateCoffeeAmount(): Float {
        val integerPart = selectedIntegerPart()
        val fractionalPart = selectedFractionalPart()
        return "$integerPart.$fractionalPart".toFloatOrNull() ?: 0f
    }
}