package com.luczka.mycoffee.domain.mappers

import com.luczka.mycoffee.data.database.entities.BrewedCoffeeEntity
import com.luczka.mycoffee.data.database.queries.BrewedCoffeeWithCoffeeRelation
import com.luczka.mycoffee.domain.models.BrewedCoffeeModel
import com.luczka.mycoffee.ui.models.BrewedCoffeeUiState

fun BrewedCoffeeModel.toUiState(): BrewedCoffeeUiState {
    return BrewedCoffeeUiState(
        coffeeAmount = coffeeAmount,
        coffee = coffee.toUiState()
    )
}

fun BrewedCoffeeUiState.toModel(): BrewedCoffeeModel {
    return BrewedCoffeeModel(
        coffeeAmount = coffeeAmount,
        coffee = coffee.toModel()
    )
}

fun BrewedCoffeeWithCoffeeRelation.toModel(): BrewedCoffeeModel {
    return BrewedCoffeeModel(
        coffeeAmount = brewedCoffeeEntity.coffeeAmount,
        coffee = coffeeEntity.toModel()
    )
}

fun BrewedCoffeeModel.toEntity(): BrewedCoffeeEntity {
    return BrewedCoffeeEntity(
        coffeeAmount = coffeeAmount,
        coffeeId = coffee.id,
        brewId = 0
    )
}