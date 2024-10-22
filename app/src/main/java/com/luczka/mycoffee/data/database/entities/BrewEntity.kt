package com.luczka.mycoffee.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class BrewEntity(
    @PrimaryKey(autoGenerate = true)
    val brewId: Int,
    val date: LocalDate,
    val coffeeAmount: Float,
    val coffeeRatio: Int,
    val waterRatio: Int,
    val waterAmount: Float,
    val rating: Int?,
    val notes: String
) {
    companion object {
        const val KEY_COLUMN = "brewId"
    }
}
