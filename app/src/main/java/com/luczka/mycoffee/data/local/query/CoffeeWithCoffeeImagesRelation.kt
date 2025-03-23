package com.luczka.mycoffee.data.local.query

import androidx.room.Embedded
import androidx.room.Relation
import com.luczka.mycoffee.data.local.entity.CoffeeEntity
import com.luczka.mycoffee.data.local.entity.CoffeeImageEntity

class CoffeeWithCoffeeImagesRelation(
    @Embedded val coffee: CoffeeEntity,
    @Relation(
        entity = CoffeeImageEntity::class,
        parentColumn = CoffeeEntity.KEY_COLUMN,
        entityColumn = CoffeeImageEntity.COFFEE_KEY_COLUMN,
    )
    val coffeeImages: List<CoffeeImageEntity>
)