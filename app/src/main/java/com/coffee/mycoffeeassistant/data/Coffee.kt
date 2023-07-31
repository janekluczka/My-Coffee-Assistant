package com.coffee.mycoffeeassistant.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.coffee.mycoffeeassistant.ui.model.CoffeeUiState
import com.coffee.mycoffeeassistant.ui.model.components.CoffeeCardUiState
import com.coffee.mycoffeeassistant.ui.model.components.DropdownMenuItemUiState
import com.coffee.mycoffeeassistant.ui.model.dateTimeFormatter
import java.time.LocalDate

@Entity(tableName = "coffee_table")
data class Coffee(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var brand: String,
    var amount: Float,
    var roast: Int?,
    var process: Int?,
    var roastingDate: String?,
    var isFavourite: Boolean,
    var hasImage: Boolean,
    var imageFile240x240: String,
    var imageFile360x360: String,
    var imageFile480x480: String,
    var imageFile720x720: String,
    var imageFile960x960: String,
) : Comparable<Coffee> {

    override fun compareTo(other: Coffee): Int {
        return compareBy<Coffee>(
            { it.name },
            { it.brand },
            { it.amount },
            { it.id }
        ).compare(this, other)
    }

    fun toCoffeeUiState(): CoffeeUiState = CoffeeUiState(
        id = id,
        name = name,
        brand = brand,
        amount = amount.toString(),
        process = process,
        roast = roast,
        roastingDate = roastingDate?.let { LocalDate.parse(it, dateTimeFormatter) },
        isFavourite = isFavourite,
        hasImage = hasImage,
        imageFile240x240 = imageFile240x240,
        imageFile360x360 = imageFile360x360,
        imageFile480x480 = imageFile480x480,
        imageFile720x720 = imageFile720x720,
        imageFile960x960 = imageFile960x960
    )

    fun toCoffeeCardUiState(): CoffeeCardUiState = CoffeeCardUiState(
        id = id,
        name = name,
        brand = brand,
        hasImage = hasImage,
        imageFile240x240 = imageFile240x240,
        imageFile360x360 = imageFile360x360,
        imageFile480x480 = imageFile480x480,
        imageFile720x720 = imageFile720x720,
        imageFile960x960 = imageFile960x960
    )

    fun toDropdownMenuItemUiState(): DropdownMenuItemUiState<Int> = DropdownMenuItemUiState(
        id = id,
        description = "$name, $brand ($amount g)",
        stringResource = null
    )

}