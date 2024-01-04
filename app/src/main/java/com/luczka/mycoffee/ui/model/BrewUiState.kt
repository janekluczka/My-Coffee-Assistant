package com.luczka.mycoffee.ui.model

import java.time.LocalDate

data class BrewUiState(
    val brewId: Int,
    val date: LocalDate,
    val coffeeAmount: Float,
    val ratio: String,
    val waterAmount: Float,
    val rating: Int?,
    val brewedCoffees: List<BrewedCoffeeUiState>
) : Comparable<BrewUiState> {
    override fun compareTo(other: BrewUiState): Int {
        return compareBy<BrewUiState>(
            { it.brewId },
            { it.date }
        ).compare(this, other)
    }
}