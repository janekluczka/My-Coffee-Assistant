package com.luczka.mycoffee.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = [
        BrewedCoffeeEntity.BREW_KEY_COLUMN,
        BrewedCoffeeEntity.COFFEE_KEY_COLUMN
    ],
    foreignKeys = [
        ForeignKey(
            entity = BrewEntity::class,
            parentColumns = [BrewEntity.KEY_COLUMN],
            childColumns = [BrewedCoffeeEntity.BREW_KEY_COLUMN],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CoffeeEntity::class,
            parentColumns = [CoffeeEntity.KEY_COLUMN],
            childColumns = [BrewedCoffeeEntity.COFFEE_KEY_COLUMN],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BrewedCoffeeEntity(
    @ColumnInfo(name = BREW_KEY_COLUMN, index = true)
    val brewId: Int,
    @ColumnInfo(name = COFFEE_KEY_COLUMN, index = true)
    val coffeeId: Int,
    val coffeeAmount: Float
) {
    companion object {
        const val BREW_KEY_COLUMN = "brewId"
        const val COFFEE_KEY_COLUMN = "coffeeId"
    }
}