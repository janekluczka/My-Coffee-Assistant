package com.luczka.mycoffee.data.mapper

import com.luczka.mycoffee.data.local.query.BrewWithBrewedCoffeesRelation
import com.luczka.mycoffee.domain.models.BrewModel

fun BrewWithBrewedCoffeesRelation.toModel() : BrewModel {
    val brewedCoffeeModels = brewedCoffeesEntity.toModel()
    return brewEntity.toModel(brewedCoffees = brewedCoffeeModels)
}

fun List<BrewWithBrewedCoffeesRelation>.toModel() : List<BrewModel> {
    return map { it.toModel() }
}