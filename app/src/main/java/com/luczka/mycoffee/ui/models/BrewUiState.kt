package com.luczka.mycoffee.ui.models

import java.time.LocalDate

data class BrewUiState(
    val brewId: Int = 0,
    val date: LocalDate,
    val coffeeAmount: Float,
    val coffeeRatio: Int,
    val waterAmount: Float,
    val waterRatio: Int,
    val rating: Int?,
    val notes: String,
    val brewedCoffees: List<BrewedCoffeeUiState>
) : Comparable<BrewUiState> {

    override fun compareTo(other: BrewUiState): Int {
        return compareBy<BrewUiState>(
            { it.brewId },
            { it.date }
        ).compare(this, other)
    }

}