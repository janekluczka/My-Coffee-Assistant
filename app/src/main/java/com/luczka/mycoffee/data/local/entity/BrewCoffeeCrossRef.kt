package com.luczka.mycoffee.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = [
        BrewCoffeeCrossRef.BREW_KEY_COLUMN,
        BrewCoffeeCrossRef.COFFEE_KEY_COLUMN
    ],
    foreignKeys = [
        ForeignKey(
            entity = BrewEntity::class,
            parentColumns = [BrewEntity.KEY_COLUMN],
            childColumns = [BrewCoffeeCrossRef.BREW_KEY_COLUMN],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CoffeeEntity::class,
            parentColumns = [CoffeeEntity.KEY_COLUMN],
            childColumns = [BrewCoffeeCrossRef.COFFEE_KEY_COLUMN],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BrewCoffeeCrossRef(
    @ColumnInfo(name = BREW_KEY_COLUMN, index = true)
    val brewId: Long = 0L,
    @ColumnInfo(name = COFFEE_KEY_COLUMN, index = true)
    val coffeeId: Long,
    val coffeeAmount: Float
) {
    companion object {
        const val BREW_KEY_COLUMN = "brewId"
        const val COFFEE_KEY_COLUMN = "coffeeId"
    }
}