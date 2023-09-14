package com.luczka.mycoffee.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.luczka.mycoffee.enums.Process
import com.luczka.mycoffee.enums.Roast
import com.luczka.mycoffee.ui.model.CoffeeUiState
import com.luczka.mycoffee.ui.model.dateTimeFormatter
import java.time.LocalDate

@Entity(tableName = "coffee_table")
data class Coffee(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var brand: String,
    var amount: Float?,
    var roast: Int?,
    var process: Int?,
    var roastingDate: String?,
    var isFavourite: Boolean,
    var imageFile240x240: String?,
    var imageFile360x360: String?,
    var imageFile480x480: String?,
    var imageFile720x720: String?,
    var imageFile960x960: String?,
) : Comparable<Coffee> {

    override fun compareTo(other: Coffee): Int {
        return compareBy<Coffee>(
            { it.isFavourite },
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
        amount = amount?.toString(),
        process = Process.values().firstOrNull { it.id == process },
        roast = Roast.values().firstOrNull { it.id == roast },
        roastingDate = roastingDate?.let { LocalDate.parse(it, dateTimeFormatter) },
        isFavourite = isFavourite,
        imageFile240x240 = imageFile240x240,
        imageFile360x360 = imageFile360x360,
        imageFile480x480 = imageFile480x480,
        imageFile720x720 = imageFile720x720,
        imageFile960x960 = imageFile960x960,
    )

}