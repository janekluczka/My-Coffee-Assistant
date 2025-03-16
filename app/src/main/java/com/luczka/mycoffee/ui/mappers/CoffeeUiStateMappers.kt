package com.luczka.mycoffee.ui.mappers

import com.luczka.mycoffee.domain.models.CoffeeModel
import com.luczka.mycoffee.ui.models.CoffeeUiState
import java.time.LocalDate

fun CoffeeModel.toUiState(): CoffeeUiState {
    return CoffeeUiState(
        coffeeId = coffeeId,
        coffeeImages = coffeeImages.toUiState(),
        roasterOrBrand = brand,
        originOrName = name,
        amount = amount.toString(),
        roast = roast?.toUiState(),
        process = process?.toUiState(),
        plantation = plantation,
        altitude = altitude?.toString(),
        scaScore = scaScore?.toString(),
        additionalInformation = additionalInformation,
        isFavourite = isFavourite,
        updatedOn = updatedOn,
        addedOn = addedOn
    )
}

fun List<CoffeeModel>.toUiState(): List<CoffeeUiState> {
    return this.map { it.toUiState() }
}

fun CoffeeUiState.toModel(): CoffeeModel {
    return CoffeeModel(
        coffeeId = coffeeId,
        coffeeImages = coffeeImages.toModel(),
        brand = roasterOrBrand,
        name = originOrName,
        amount = if (amount.isBlank()) 0f else amount.toFloat(),
        roast = roast?.toModel(),
        process = process?.toModel(),
        plantation = plantation,
        altitude = altitude?.toIntOrNull(),
        scaScore = scaScore?.toFloatOrNull(),
        additionalInformation = additionalInformation,
        isFavourite = isFavourite,
        updatedOn = LocalDate.now(),
        addedOn = addedOn ?: LocalDate.now()
    )
}