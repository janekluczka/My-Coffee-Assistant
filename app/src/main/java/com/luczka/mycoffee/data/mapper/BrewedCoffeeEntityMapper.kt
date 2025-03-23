package com.luczka.mycoffee.data.mapper

import com.luczka.mycoffee.data.local.entity.BrewCoffeeCrossRef
import com.luczka.mycoffee.data.local.query.BrewedCoffeeWithCoffeeRelation
import com.luczka.mycoffee.domain.models.BrewedCoffeeModel

fun BrewedCoffeeModel.toEntity(): BrewCoffeeCrossRef {
    return BrewCoffeeCrossRef(
        coffeeAmount = coffeeAmount,
        coffeeId = coffee.coffeeId,
    )
}

fun BrewedCoffeeWithCoffeeRelation.toModel(): BrewedCoffeeModel {
    return BrewedCoffeeModel(
        coffeeAmount = brewCoffeeCrossRef.coffeeAmount,
        coffee = coffeeEntity.toModel()
    )
}

fun List<BrewedCoffeeWithCoffeeRelation>.toModel(): List<BrewedCoffeeModel> {
    return this.map { it.toModel() }
}