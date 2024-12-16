package com.luczka.mycoffee.data.mappers

import com.luczka.mycoffee.data.database.queries.BrewWithBrewedCoffeesRelation
import com.luczka.mycoffee.domain.models.BrewModel

fun BrewWithBrewedCoffeesRelation.toModel() : BrewModel {
    val brewedCoffeeModels = brewedCoffeesEntity.map { it.toModel() }
    return brewEntity.toModel(brewedCoffees = brewedCoffeeModels)
}