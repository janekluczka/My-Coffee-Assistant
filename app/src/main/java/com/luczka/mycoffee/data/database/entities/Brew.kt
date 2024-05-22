package com.luczka.mycoffee.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Brew(
    @PrimaryKey(autoGenerate = true)
    val brewId: Int,
    val date: String,
    val coffeeAmount: Float,
    val coffeeRatio: Int,
    val waterRatio: Int,
    val waterAmount: Float,
    val rating: Int?,
    val notes: String
)