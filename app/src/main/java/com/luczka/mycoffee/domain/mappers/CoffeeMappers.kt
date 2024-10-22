package com.luczka.mycoffee.domain.mappers

import com.luczka.mycoffee.data.database.entities.CoffeeEntity
import com.luczka.mycoffee.domain.models.CoffeeModel
import com.luczka.mycoffee.ui.models.CoffeeUiState

fun CoffeeModel.toUiState(): CoffeeUiState {
    return CoffeeUiState(
        coffeeId = id,
        name = name,
        brand = brand,
        amount = amount?.toString(),
        scaScore = scaScore?.toString(),
        roast = roast?.toUiState(),
        process = process?.toUiState(),
        isFavourite = isFavourite,
        imageFile240x240 = imageFile240x240,
        imageFile360x360 = imageFile360x360,
        imageFile480x480 = imageFile480x480,
        imageFile720x720 = imageFile720x720,
        imageFile960x960 = imageFile960x960
    )
}

fun CoffeeUiState.toModel(): CoffeeModel {
    return CoffeeModel(
        id = coffeeId,
        name = name,
        brand = brand,
        amount = amount?.toFloatOrNull(),
        scaScore = scaScore?.toFloatOrNull(),
        roast = roast?.toModel(),
        process = process?.toModel(),
        isFavourite = isFavourite,
        imageFile240x240 = imageFile240x240,
        imageFile360x360 = imageFile360x360,
        imageFile480x480 = imageFile480x480,
        imageFile720x720 = imageFile720x720,
        imageFile960x960 = imageFile960x960
    )
}

fun CoffeeEntity.toModel(): CoffeeModel {
    return CoffeeModel(
        id = coffeeId,
        name = name,
        brand = brand,
        amount = amount,
        scaScore = scaScore,
        roast = roast?.toModel(),
        process = process?.toModel(),
        isFavourite = isFavourite,
        imageFile240x240 = imageFile240x240,
        imageFile360x360 = imageFile360x360,
        imageFile480x480 = imageFile480x480,
        imageFile720x720 = imageFile720x720,
        imageFile960x960 = imageFile960x960
    )
}

fun CoffeeModel.toEntity(): CoffeeEntity {
    return CoffeeEntity(
        coffeeId = id,
        name = name,
        brand = brand,
        amount = amount,
        scaScore = scaScore,
        roast = roast?.toType(),
        process = process?.toType(),
        isFavourite = isFavourite,
        imageFile240x240 = imageFile240x240,
        imageFile360x360 = imageFile360x360,
        imageFile480x480 = imageFile480x480,
        imageFile720x720 = imageFile720x720,
        imageFile960x960 = imageFile960x960
    )
}
