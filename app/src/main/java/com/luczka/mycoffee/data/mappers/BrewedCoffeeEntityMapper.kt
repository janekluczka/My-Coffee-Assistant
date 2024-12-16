package com.luczka.mycoffee.data.mappers

import com.luczka.mycoffee.data.database.entities.BrewCoffeeCrossRef
import com.luczka.mycoffee.data.database.queries.BrewedCoffeeWithCoffeeRelation
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