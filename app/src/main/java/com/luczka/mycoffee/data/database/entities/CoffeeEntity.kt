package com.luczka.mycoffee.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.luczka.mycoffee.data.database.types.ProcessType
import com.luczka.mycoffee.data.database.types.RoastType

/**
 * Represents a coffee item in the data layer.
 *
 * @property coffeeId The unique ID of the coffee, auto-generated by the database.
 * @property name The name of the coffee.
 * @property brand The brand of the coffee.
 * @property amount The amount of coffee in grams, if specified.
 * @property scaScore The SCA (Specialty Coffee Association) score of the coffee, if available.
 * @property roast The roast level of the coffee, represented by the RoastType enum.
 * @property process The processing method of the coffee, represented by the ProcessType enum.
 * @property isFavourite Indicates whether the coffee is marked as a favorite.
 * @property imageFile240x240 The file path to the 240x240 resolution image of the coffee, if available.
 * @property imageFile360x360 The file path to the 360x360 resolution image of the coffee, if available.
 * @property imageFile480x480 The file path to the 480x480 resolution image of the coffee, if available.
 * @property imageFile720x720 The file path to the 720x720 resolution image of the coffee, if available.
 * @property imageFile960x960 The file path to the 960x960 resolution image of the coffee, if available.
 */
@Entity
data class  CoffeeEntity(
    @PrimaryKey(autoGenerate = true)
    val coffeeId: Int,
    val name: String,
    val brand: String,
    val amount: Float?,
    val scaScore: Float?,
    val roast: RoastType?,
    val process: ProcessType?,
    val isFavourite: Boolean,
    val imageFile240x240: String?,
    val imageFile360x360: String?,
    val imageFile480x480: String?,
    val imageFile720x720: String?,
    val imageFile960x960: String?,
) : Comparable<CoffeeEntity> {

    override fun compareTo(other: CoffeeEntity): Int {
        return compareBy<CoffeeEntity>(
            { it.isFavourite },
            { it.name },
            { it.brand },
            { it.amount },
            { it.coffeeId }
        ).compare(this, other)
    }

    companion object {
        const val KEY_COLUMN = "coffeeId"
    }

}