package com.luczka.mycoffee.data.local.query

import androidx.room.Embedded
import androidx.room.Relation
import com.luczka.mycoffee.data.local.entity.BrewCoffeeCrossRef
import com.luczka.mycoffee.data.local.entity.BrewEntity

data class BrewWithBrewedCoffeesRelation(
    @Embedded val brewEntity: BrewEntity,
    @Relation(
        parentColumn = BrewEntity.KEY_COLUMN,
        entityColumn = BrewCoffeeCrossRef.BREW_KEY_COLUMN,
        entity = BrewCoffeeCrossRef::class
    )
    val brewedCoffeesEntity: List<BrewedCoffeeWithCoffeeRelation>
)