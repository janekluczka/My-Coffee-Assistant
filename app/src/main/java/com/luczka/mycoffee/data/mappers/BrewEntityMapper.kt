package com.luczka.mycoffee.data.mappers

import com.luczka.mycoffee.data.database.entities.BrewEntity
import com.luczka.mycoffee.domain.models.BrewModel
import com.luczka.mycoffee.domain.models.BrewedCoffeeModel

fun BrewModel.toEntity(): BrewEntity {
    return BrewEntity(
        brewId = id,
        date = date,
        coffeeAmount = coffeeAmount,
        coffeeRatio = coffeeRatio,
        waterRatio = waterRatio,
        waterAmount = waterAmount,
        rating = rating,
        notes = notes
    )
}

fun BrewEntity.toModel(brewedCoffees: List<BrewedCoffeeModel>): BrewModel {
    return BrewModel(
        id = brewId,
        date = date,
        coffeeAmount = coffeeAmount,
        coffeeRatio = coffeeRatio,
        waterRatio = waterRatio,
        waterAmount = waterAmount,
        rating = rating,
        notes = notes,
        brewedCoffees = brewedCoffees
    )
}