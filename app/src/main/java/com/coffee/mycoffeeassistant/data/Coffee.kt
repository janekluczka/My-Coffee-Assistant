package com.coffee.mycoffeeassistant.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coffee_table")
data class Coffee(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var brand: String,
    var currentAmount: Float,
    var startAmount: Float,
    var roast: String,
    var process: String,
//    var roastingDate:
)