package com.luczka.mycoffee.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CoffeeEntity::class,
            parentColumns = [CoffeeEntity.KEY_COLUMN],
            childColumns = [CoffeeImageEntity.COFFEE_KEY_COLUMN],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = [CoffeeImageEntity.COFFEE_KEY_COLUMN])]
)
data class CoffeeImageEntity(
    @PrimaryKey(autoGenerate = true)
    val coffeeImageId: Long = 0L,
    val uri: String,
    val filename: String,
    val index: Int,
    val coffeeId: Long = 0L,
) {
    companion object {
        const val COFFEE_KEY_COLUMN = "coffeeId"
    }
}
