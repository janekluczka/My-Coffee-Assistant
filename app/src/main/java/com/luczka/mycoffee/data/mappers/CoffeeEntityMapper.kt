package com.luczka.mycoffee.data.mappers

import com.luczka.mycoffee.data.database.entities.CoffeeEntity
import com.luczka.mycoffee.domain.models.CoffeeImageModel
import com.luczka.mycoffee.domain.models.CoffeeModel

fun CoffeeModel.toEntity(): CoffeeEntity {
    return CoffeeEntity(
        coffeeId = coffeeId,
        brand = brand,
        name = name,
        amount = amount,
        roast = roast?.toType(),
        process = process?.toType(),
        plantation = plantation,
        altitude = altitude,
        scaScore = scaScore,
        additionalInformation = additionalInformation,
        isFavourite = isFavourite,
        updatedOn = updatedOn,
        addedOn = addedOn
    )
}

fun CoffeeEntity.toModel(coffeeImages: List<CoffeeImageModel>): CoffeeModel {
    return CoffeeModel(
        coffeeId = coffeeId,
        coffeeImages = coffeeImages,
        brand = brand,
        name = name,
        amount = amount,
        roast = roast?.toModel(),
        process = process?.toModel(),
        plantation = plantation,
        altitude = altitude,
        scaScore = scaScore,
        additionalInformation = additionalInformation,
        isFavourite = isFavourite,
        updatedOn = updatedOn,
        addedOn = addedOn
    )
}
