package com.luczka.mycoffee.data.database

import androidx.room.Embedded
import androidx.room.Relation
import com.luczka.mycoffee.data.database.entities.Brew
import com.luczka.mycoffee.data.database.entities.BrewedCoffee
import com.luczka.mycoffee.data.database.entities.Coffee
import com.luczka.mycoffee.ui.model.BrewUiState
import com.luczka.mycoffee.ui.model.BrewedCoffeeUiState
import com.luczka.mycoffee.util.LocalDateParser

data class BrewWithBrewedCoffees(
    @Embedded val brew: Brew,
    @Relation(
        parentColumn = "brewId",
        entityColumn = "brewId",
        entity = BrewedCoffee::class
    )
    val brewedCoffees: List<BrewedCoffeeWithCoffee>
) {
    fun toBrewUiState(): BrewUiState = BrewUiState(
        brewId = brew.brewId,
        date = LocalDateParser.parseBasicIsoDate(brew.date),
        coffeeAmount = brew.coffeeAmount,
        coffeeRatio = brew.coffeeRatio,
        waterAmount = brew.waterAmount,
        waterRatio = brew.waterRatio,
        rating = brew.rating,
        notes = brew.notes,
        brewedCoffees = brewedCoffees.map { it.toBrewedCoffeeUiState() }
    )
}

data class BrewedCoffeeWithCoffee(
    @Embedded val brewedCoffee: BrewedCoffee,
    @Relation(
        parentColumn = "coffeeId",
        entityColumn = "coffeeId"
    )
    val coffee: Coffee
) {
    fun toBrewedCoffeeUiState(): BrewedCoffeeUiState = BrewedCoffeeUiState(
        coffeeAmount = brewedCoffee.coffeeAmount,
        coffee = coffee.toCoffeeUiState()
    )
}