package com.coffee.mycoffeeassistant.ui.model

import android.graphics.Bitmap
import android.net.Uri
import com.coffee.mycoffeeassistant.data.Coffee
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.BASIC_ISO_DATE

data class CoffeeUiState(
    val roastOptions: List<String> = listOf("Unknown", "Light", "Medium", "Medium Dark", "Dark"),
    val processOptions: List<String> = listOf("Unknown", "Natural", "Washed", "Honey"),
    val id: Int = 0,
    var name: String = "",
    var brand: String = "",
    var currentAmount: String = "",
    var startAmount: String = "",
    var roast: String = roastOptions[0],
    var process: String = processOptions[0],
    var roastingDate: LocalDate = LocalDate.now(),
    var isFavourite: Boolean = false,
    var imageUri: Uri = Uri.EMPTY,
    var image: ByteArray = byteArrayOf(),
    var bitmap: Bitmap? = null,
) : Comparable<CoffeeUiState> {
    override fun compareTo(other: CoffeeUiState): Int {
        return compareBy<CoffeeUiState>(
            { it.name },
            { it.brand },
            { it.currentAmount },
            { it.id }
        ).compare(this, other)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CoffeeUiState
        if (roastOptions != other.roastOptions) return false
        if (processOptions != other.processOptions) return false
        if (id != other.id) return false
        if (name != other.name) return false
        if (brand != other.brand) return false
        if (currentAmount != other.currentAmount) return false
        if (startAmount != other.startAmount) return false
        if (roast != other.roast) return false
        if (process != other.process) return false
        if (roastingDate != other.roastingDate) return false
        if (isFavourite != other.isFavourite) return false
        if (imageUri != other.imageUri) return false
        if (!image.contentEquals(other.image)) return false
        if (bitmap != other.bitmap) return false
        return true
    }

    override fun hashCode(): Int {
        var result = roastOptions.hashCode()
        result = 31 * result + processOptions.hashCode()
        result = 31 * result + id
        result = 31 * result + name.hashCode()
        result = 31 * result + brand.hashCode()
        result = 31 * result + currentAmount.hashCode()
        result = 31 * result + startAmount.hashCode()
        result = 31 * result + roast.hashCode()
        result = 31 * result + process.hashCode()
        result = 31 * result + roastingDate.hashCode()
        result = 31 * result + isFavourite.hashCode()
        result = 31 * result + imageUri.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + (bitmap?.hashCode() ?: 0)
        return result
    }
}

fun CoffeeUiState.toCoffee(): Coffee = Coffee(
    id = id,
    name = name,
    brand = brand,
    currentAmount = currentAmount.toFloat(),
    startAmount = startAmount.toFloat(),
    roast = roast,
    process = process,
    roastingDate = roastingDate.format(dateTimeFormatter),
    isFavourite = isFavourite,
    imageUri = imageUri.toString(),
    image = image
)

fun CoffeeUiState.markWrongFields(): AddCoffeeUiState = AddCoffeeUiState(
    isNameWrong = name.isEmpty() || name.isBlank(),
    isBrandWrong = brand.isEmpty() || brand.isBlank(),
    isAmountWrong = currentAmount.toIntOrNull() == null || currentAmount.toInt() <= 0,
)

fun CoffeeUiState.isValid(): Boolean =
    name.isNotEmpty() &&
            name.isNotBlank() &&
            brand.isNotEmpty() &&
            brand.isNotBlank() &&
            currentAmount.toIntOrNull() != null &&
            currentAmount.toInt() > 0

