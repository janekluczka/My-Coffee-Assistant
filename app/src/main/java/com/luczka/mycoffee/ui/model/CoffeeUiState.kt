package com.luczka.mycoffee.ui.model

import com.luczka.mycoffee.data.database.entities.Coffee
import com.luczka.mycoffee.enum.Process
import com.luczka.mycoffee.enum.Roast

data class CoffeeUiState(
    val coffeeId: Int = 0,
    val name: String = "",
    val brand: String = "",
    val amount: String? = null,
    val scaScore: String? = null,
    val process: Process? = null,
    val roast: Roast? = null,
    val isFavourite: Boolean = false,
    val imageFile240x240: String? = null,
    val imageFile360x360: String? = null,
    val imageFile480x480: String? = null,
    val imageFile720x720: String? = null,
    val imageFile960x960: String? = null,
) : Comparable<CoffeeUiState> {

    override fun compareTo(other: CoffeeUiState): Int {
        return compareBy<CoffeeUiState>(
            { it.name },
            { it.brand },
            { it.amount },
            { it.coffeeId }
        ).compare(this, other)
    }

    fun toCoffee(): Coffee = Coffee(
        coffeeId = coffeeId,
        name = name.trim(),
        brand = brand.trim(),
        amount = amount?.toFloatOrNull(),
        scaScore = scaScore?.toFloatOrNull(),
        process = process?.id,
        roast = roast?.id,
        isFavourite = isFavourite,
        imageFile240x240 = imageFile240x240,
        imageFile360x360 = imageFile360x360,
        imageFile480x480 = imageFile480x480,
        imageFile720x720 = imageFile720x720,
        imageFile960x960 = imageFile960x960,
    )

    fun isBlank(): Boolean {
        if (coffeeId != 0) return false
        if (name != "") return false
        if (brand != "") return false
        if (amount != null) return false
        if (process != null) return false
        if (roast != null) return false
        if (imageFile240x240 != null) return false
        if (imageFile360x360 != null) return false
        if (imageFile480x480 != null) return false
        if (imageFile720x720 != null) return false
        if (imageFile960x960 != null) return false
        return true
    }

    fun isNameWrong(): Boolean = name.isBlank()

    fun isBrandWrong(): Boolean = brand.isBlank()

    fun hasAmount(): Boolean = amount != null

    fun hasAmountLowerThan(amount: Float): Boolean {
        val amountFloat = this.amount?.toFloatOrNull() ?: return false
        return amountFloat < amount
    }

    override fun toString(): String = "$name, $brand ($amount\u00A0g)"

}