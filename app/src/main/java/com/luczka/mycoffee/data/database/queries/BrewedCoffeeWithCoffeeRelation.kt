package com.luczka.mycoffee.data.database.queries

import androidx.room.Embedded
import androidx.room.Relation
import com.luczka.mycoffee.data.database.entities.BrewCoffeeCrossRef
import com.luczka.mycoffee.data.database.entities.CoffeeEntity

data class BrewedCoffeeWithCoffeeRelation(
    @Embedded val brewCoffeeCrossRef: BrewCoffeeCrossRef,
    @Relation(
        parentColumn = BrewCoffeeCrossRef.COFFEE_KEY_COLUMN,
        entityColumn = CoffeeEntity.KEY_COLUMN,
        entity = CoffeeEntity::class
    )
    val coffeeEntity: CoffeeWithCoffeeImagesRelation
)