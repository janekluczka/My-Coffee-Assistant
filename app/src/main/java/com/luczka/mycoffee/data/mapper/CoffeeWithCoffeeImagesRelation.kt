package com.luczka.mycoffee.data.mapper

import com.luczka.mycoffee.data.local.query.CoffeeWithCoffeeImagesRelation
import com.luczka.mycoffee.domain.models.CoffeeModel

fun CoffeeWithCoffeeImagesRelation.toModel(): CoffeeModel {
    val coffeeImageModels = coffeeImages.toModel()
    return coffee.toModel(coffeeImageModels)
}

fun List<CoffeeWithCoffeeImagesRelation>.toModel(): List<CoffeeModel> {
    return map { it.toModel() }
}