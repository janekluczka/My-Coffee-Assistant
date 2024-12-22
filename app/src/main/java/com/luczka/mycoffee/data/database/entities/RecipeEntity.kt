package com.luczka.mycoffee.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val recipeId: Long,
    val coffeeAmount: Double,
    val coffeeRatio: Int,
    val waterRatio: Int,
    val waterAmount: Double,
)
