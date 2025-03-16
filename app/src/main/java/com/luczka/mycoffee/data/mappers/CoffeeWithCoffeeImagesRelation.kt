package com.luczka.mycoffee.data.mappers

import com.luczka.mycoffee.data.database.queries.CoffeeWithCoffeeImagesRelation
import com.luczka.mycoffee.domain.models.CoffeeModel

fun CoffeeWithCoffeeImagesRelation.toModel(): CoffeeModel {
    val coffeeImageModels = coffeeImages.toModel()
    return coffee.toModel(coffeeImageModels)
}