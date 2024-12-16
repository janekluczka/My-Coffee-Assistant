package com.luczka.mycoffee.ui.models

import java.time.LocalDate

data class BrewUiState(
    val brewId: Long = 0L,
    val addedOn: LocalDate,
    val formattedDate: String = "",
    val coffeeAmount: Float,
    val coffeeRatio: Int,
    val waterAmount: Float,
    val waterRatio: Int,
    val rating: Int? = null,
    val notes: String = "",
    val brewedCoffees: List<BrewedCoffeeUiState>
) : Comparable<BrewUiState> {

    override fun compareTo(other: BrewUiState): Int {
        return compareBy<BrewUiState>(
            { it.addedOn },
            { it.brewId },
        ).compare(this, other)
    }

}