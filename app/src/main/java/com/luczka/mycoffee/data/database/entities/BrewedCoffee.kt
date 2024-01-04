package com.luczka.mycoffee.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["brewId", "coffeeId"],
    foreignKeys = [
        ForeignKey(
            entity = Brew::class,
            parentColumns = ["brewId"],
            childColumns = ["brewId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Coffee::class,
            parentColumns = ["coffeeId"],
            childColumns = ["coffeeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BrewedCoffee(
    val brewId: Int,
    val coffeeId: Int,
    val coffeeAmount: Float
)