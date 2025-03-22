package com.luczka.mycoffee.ui.models

import java.time.LocalDate

data class CoffeeUiState(
    val coffeeId: Long = 0L,
    val coffeeImages: List<CoffeeImageUiState> = emptyList(),
    val roasterOrBrand: String = "",
    val originOrName: String = "",
    val amount: String = "",
    val process: ProcessUiState? = null,
    val roast: RoastUiState? = null,
    val plantation: String? = null,
    val altitude: String? = null,
    val scaScore: String? = null,
    val additionalInformation: String? = null,
    val isFavourite: Boolean = false,
    val updatedOn: LocalDate? = null,
    val addedOn: LocalDate? = null
) : Comparable<CoffeeUiState> {

    override fun compareTo(other: CoffeeUiState): Int {
        return compareBy<CoffeeUiState>(
            { it.originOrName },
            { it.roasterOrBrand },
            { it.amount },
            { it.coffeeId }
        ).compare(this, other)
    }

    fun isBlank(): Boolean {
        if (coffeeId != 0L) return false
        if (coffeeImages.isNotEmpty()) return false
        if (roasterOrBrand.isNotBlank()) return false
        if (originOrName.isNotBlank()) return false
        if (amount.isNotBlank()) return false
        if (roast != null) return false
        if (process != null) return false
        if (plantation != null) return false
        if (altitude != null) return false
        if (scaScore != null) return false
        if (additionalInformation != null) return false
        if (isFavourite) return false
        if (updatedOn != null) return false
        if (addedOn != null) return false
        return true
    }
}