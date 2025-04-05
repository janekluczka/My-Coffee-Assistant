package com.luczka.mycoffee.ui.screens.brewassistant.state

import androidx.annotation.StringRes
import com.luczka.mycoffee.R

data class BrewAssistantRatioItemUiState(
    val openPicker: Boolean = false,
    val openDialog: Boolean = false,
    val coffeeRatioIndex: Int = 0,
    val coffeeRatioItems: List<Int> = (1..10).toList(),
    val coffeeRatioItemsTextFormatter: ((Int) -> String)? = null,
    val waterRatioIndex: Int = 0,
    val waterRatioItems: List<Int> = (1..100).toList(),
    val waterRatioItemsTextFormatter: ((Int) -> String)? = null,
    @StringRes val separatorRes: Int = R.string.separator_ratio,
) {
    fun selectedCoffeeRatio(): Int {
        return coffeeRatioItems[coffeeRatioIndex]
    }

    fun selectedWaterRatio(): Int {
        return waterRatioItems[waterRatioIndex]
    }
}