package com.luczka.mycoffee.data.database.queries

import androidx.room.Embedded
import androidx.room.Relation
import com.luczka.mycoffee.data.database.entities.CoffeeEntity
import com.luczka.mycoffee.data.database.entities.CoffeeImageEntity

class CoffeeWithCoffeeImagesRelation(
    @Embedded val coffee: CoffeeEntity,
    @Relation(
        entity = CoffeeImageEntity::class,
        parentColumn = CoffeeEntity.KEY_COLUMN,
        entityColumn = CoffeeImageEntity.COFFEE_KEY_COLUMN,
    )
    val coffeeImages: List<CoffeeImageEntity>
)