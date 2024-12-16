package com.luczka.mycoffee.data.mappers

import android.net.Uri
import com.luczka.mycoffee.data.database.entities.CoffeeImageEntity
import com.luczka.mycoffee.domain.models.CoffeeImageModel

fun CoffeeImageModel.toEntity() : CoffeeImageEntity {
    return CoffeeImageEntity(
        coffeeImageId = coffeeImageId,
        uri = uri.toString(),
        filename = filename,
        index = index
    )
}

fun CoffeeImageEntity.toModel() : CoffeeImageModel {
    return CoffeeImageModel(
        coffeeImageId = coffeeImageId,
        uri = Uri.parse(uri),
        filename = filename,
        index = index
    )
}