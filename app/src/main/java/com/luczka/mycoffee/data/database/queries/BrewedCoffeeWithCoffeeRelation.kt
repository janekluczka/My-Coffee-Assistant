package com.luczka.mycoffee.data.database.queries

import androidx.room.Embedded
import androidx.room.Relation
import com.luczka.mycoffee.data.database.entities.BrewedCoffeeEntity
import com.luczka.mycoffee.data.database.entities.CoffeeEntity

data class BrewedCoffeeWithCoffeeRelation(
    @Embedded val brewedCoffeeEntity: BrewedCoffeeEntity,
    @Relation(
        parentColumn = BrewedCoffeeEntity.COFFEE_KEY_COLUMN,
        entityColumn = CoffeeEntity.KEY_COLUMN,
    )
    val coffeeEntity: CoffeeEntity
)