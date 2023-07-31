package com.coffee.mycoffeeassistant.ui.model

import com.coffee.mycoffeeassistant.data.Coffee
import com.coffee.mycoffeeassistant.util.isNotPositiveFloat
import com.coffee.mycoffeeassistant.util.isPositiveFloat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.BASIC_ISO_DATE

data class CoffeeUiState(
    val id: Int = 0,
    var name: String = "",
    var brand: String = "",
    var amount: String = "",
    var process: Int? = null,
    var roast: Int? = null,
    var roastingDate: LocalDate? = null,
    var isFavourite: Boolean = false,
    val hasImage: Boolean = false,
    var imageFile240x240: String = "",
    var imageFile360x360: String = "",
    var imageFile480x480: String = "",
    var imageFile720x720: String = "",
    var imageFile960x960: String = "",
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
        name = name,
        brand = brand,
        amount = amount.toFloat(),
        process = process,
        roast = roast,
        roastingDate = roastingDate?.format(dateTimeFormatter),
        isFavourite = isFavourite,
        hasImage = hasImage,
        imageFile240x240 = imageFile240x240,
        imageFile360x360 = imageFile360x360,
        imageFile480x480 = imageFile480x480,
        imageFile720x720 = imageFile720x720,
        imageFile960x960 = imageFile960x960
    )

    fun isBlank(): Boolean {
        if (name != "") return false
        if (brand != "") return false
        if (amount != "") return false
        if (process == null) return false
        if (roast == null) return false
        if (roastingDate != LocalDate.now()) return false
        if (hasImage) return false
        return true
    }

    fun isNameWrong(): Boolean = name.isEmpty() || name.isBlank()
    fun isBrandWrong(): Boolean = brand.isEmpty() || brand.isBlank()
    fun isAmountWrong(): Boolean = amount.isNotPositiveFloat()

    fun isValid(): Boolean =
        name.isNotEmpty() &&
                name.isNotBlank() &&
                brand.isNotEmpty() &&
                brand.isNotBlank() &&
                amount.toIntOrNull() != null &&
                amount.toInt() > 0

    fun isInStock(): Boolean = amount.isPositiveFloat()

    fun isOnlyInFavourites(): Boolean = amount.isNotPositiveFloat() && isFavourite

    override fun toString(): String = "$name, $brand ($amount g)"

}