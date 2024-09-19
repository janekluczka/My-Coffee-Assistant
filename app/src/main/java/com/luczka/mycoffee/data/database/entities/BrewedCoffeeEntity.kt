package com.luczka.mycoffee.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["brewId", "coffeeId"],
    foreignKeys = [
        ForeignKey(
            entity = BrewEntity::class,
            parentColumns = ["brewId"],
            childColumns = ["brewId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CoffeeEntity::class,
            parentColumns = ["coffeeId"],
            childColumns = ["coffeeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BrewedCoffeeEntity(
    val brewId: Int,
    val coffeeId: Int,
    val coffeeAmount: Float
)