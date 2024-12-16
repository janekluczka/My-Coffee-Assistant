package com.luczka.mycoffee.ui.mappers

import com.luczka.mycoffee.domain.models.BrewModel
import com.luczka.mycoffee.ui.models.BrewUiState
import com.luczka.mycoffee.ui.util.DateFormattingUtil

fun BrewUiState.toModel(): BrewModel {
    return BrewModel(
        id = brewId,
        date = addedOn,
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
        addedOn = date,
        formattedDate = DateFormattingUtil.formatShortDate(date),
        coffeeAmount = coffeeAmount,
        coffeeRatio = coffeeRatio,
        waterRatio = waterRatio,
        waterAmount = waterAmount,
        rating = rating,
        notes = notes,
        brewedCoffees = brewedCoffees.map { it.toUiState() }
    )
}