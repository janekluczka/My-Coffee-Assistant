package com.luczka.mycoffee.ui.model

import com.luczka.mycoffee.data.Coffee
import com.luczka.mycoffee.enums.Process
import com.luczka.mycoffee.enums.Roast
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.BASIC_ISO_DATE

data class CoffeeUiState(
    val id: Int = 0,
    var name: String = "",
    var brand: String = "",
    var amount: String? = null,
    var process: Process? = null,
    var roast: Roast? = null,
    var roastingDate: LocalDate? = null,
    var isFavourite: Boolean = false,
    var imageFile240x240: String? = null,
    var imageFile360x360: String? = null,
    var imageFile480x480: String? = null,
    var imageFile720x720: String? = null,
    var imageFile960x960: String? = null,
) : Comparable<CoffeeUiState> {
    override fun compareTo(other: CoffeeUiState): Int {
        return compareBy<CoffeeUiState>(
            { it.name },
            { it.brand },
            { it.amount },
            { it.id }
        ).compare(this, other)
    }

    fun toCoffee(): Coffee = Coffee(
        id = id,
        name = name.trim(),
        brand = brand.trim(),
        amount = amount?.toFloat(),
        process = process?.id,
        roast = roast?.id,
        roastingDate = roastingDate?.format(dateTimeFormatter),
        isFavourite = isFavourite,
        imageFile240x240 = imageFile240x240,
        imageFile360x360 = imageFile360x360,
        imageFile480x480 = imageFile480x480,
        imageFile720x720 = imageFile720x720,
        imageFile960x960 = imageFile960x960,
    )

    fun isBlank(): Boolean {
        if (id != 0) return false
        if (name != "") return false
        if (brand != "") return false
        if (amount != null) return false
        if (process != null) return false
        if (roast != null) return false
        if (roastingDate != null) return false
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

    fun isOlderThan(date: LocalDate): Boolean {
        val roastingDate = this.roastingDate ?: return false
        return roastingDate.isBefore(date)
    }

    override fun toString(): String = "$name, $brand ($amount\u00A0g)"

}