package com.luczka.mycoffee.domain.mappers

import com.luczka.mycoffee.data.database.entities.BrewEntity
import com.luczka.mycoffee.data.database.queries.BrewWithBrewedCoffeesRelation
import com.luczka.mycoffee.domain.models.BrewModel
import com.luczka.mycoffee.domain.models.BrewedCoffeeModel
import com.luczka.mycoffee.ui.models.BrewUiState

fun BrewUiState.toModel(): BrewModel {
    return BrewModel(
        id = brewId,
        date = date,
        coffeeAmount = coffeeAmount,
        coffeeRatio = coffeeRatio,
        waterRatio = waterRatio,
        waterAmount = waterAmount,
        rating = rating,
        notes = notes,
        brewedCoffees = brewedCoffees.map { it.toModel() }
    )
}

fun BrewModel.toUiState() : BrewUiState {
    return BrewUiState(
        brewId = id,
        date = date,
        coffeeAmount = coffeeAmount,
        coffeeRatio = coffeeRatio,
        waterRatio = waterRatio,
        waterAmount = waterAmount,
        rating = rating,
        notes = notes,
        brewedCoffees = brewedCoffees.map { it.toUiState() }
    )
}

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

fun BrewWithBrewedCoffeesRelation.toModel() : BrewModel {
    return brewEntity.toModel(brewedCoffees = brewedCoffeesEntity.map { it.toModel() })
}

private fun BrewEntity.toModel(brewedCoffees: List<BrewedCoffeeModel>): BrewModel {
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