package com.luczka.mycoffee.data.local.query

import androidx.room.Embedded
import androidx.room.Relation
import com.luczka.mycoffee.data.local.entity.BrewCoffeeCrossRef
import com.luczka.mycoffee.data.local.entity.CoffeeEntity

data class BrewedCoffeeWithCoffeeRelation(
    @Embedded val brewCoffeeCrossRef: BrewCoffeeCrossRef,
    @Relation(
        parentColumn = BrewCoffeeCrossRef.COFFEE_KEY_COLUMN,
        entityColumn = CoffeeEntity.KEY_COLUMN,
        entity = CoffeeEntity::class
    )
    val coffeeEntity: CoffeeWithCoffeeImagesRelation
)