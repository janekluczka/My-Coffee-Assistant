package com.luczka.mycoffee.data.database.queries

import androidx.room.Embedded
import androidx.room.Relation
import com.luczka.mycoffee.data.database.entities.BrewEntity
import com.luczka.mycoffee.data.database.entities.BrewedCoffeeEntity

data class BrewWithBrewedCoffeesRelation(
    @Embedded val brewEntity: BrewEntity,
    @Relation(
        parentColumn = BrewEntity.KEY_COLUMN,
        entityColumn = BrewedCoffeeEntity.BREW_KEY_COLUMN,
        entity = BrewedCoffeeEntity::class
    )
    val brewedCoffeesEntity: List<BrewedCoffeeWithCoffeeRelation>
)