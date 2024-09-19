package com.luczka.mycoffee.data.database

import androidx.room.Embedded
import androidx.room.Relation
import com.luczka.mycoffee.data.database.entities.BrewEntity
import com.luczka.mycoffee.data.database.entities.BrewedCoffeeEntity
import com.luczka.mycoffee.data.database.entities.CoffeeEntity
import com.luczka.mycoffee.ui.screen.history.BrewUiState
import com.luczka.mycoffee.ui.screen.history.BrewedCoffeeUiState
import com.luczka.mycoffee.util.LocalDateParser

data class BrewWithBrewedCoffees(
    @Embedded val brewEntity: BrewEntity,
    @Relation(
        parentColumn = "brewId",
        entityColumn = "brewId",
        entity = BrewedCoffeeEntity::class
    )
    val brewedCoffeesEntity: List<BrewedCoffeeWithCoffee>
) {
    fun toBrewUiState(): BrewUiState = BrewUiState(
        brewId = brewEntity.brewId,
        date = LocalDateParser.parseBasicIsoDate(brewEntity.date),
        coffeeAmount = brewEntity.coffeeAmount,
        coffeeRatio = brewEntity.coffeeRatio,
        waterAmount = brewEntity.waterAmount,
        waterRatio = brewEntity.waterRatio,
        rating = brewEntity.rating,
        notes = brewEntity.notes,
        brewedCoffees = brewedCoffeesEntity.map { it.toBrewedCoffeeUiState() }
    )
}

data class BrewedCoffeeWithCoffee(
    @Embedded val brewedCoffeeEntity: BrewedCoffeeEntity,
    @Relation(
        parentColumn = "coffeeId",
        entityColumn = "coffeeId"
    )
    val coffeeEntity: CoffeeEntity
) {
    fun toBrewedCoffeeUiState(): BrewedCoffeeUiState = BrewedCoffeeUiState(
        coffeeAmount = brewedCoffeeEntity.coffeeAmount,
        coffee = coffeeEntity.toCoffeeUiState()
    )
}