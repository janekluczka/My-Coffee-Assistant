package com.luczka.mycoffee.ui.mappers

import com.luczka.mycoffee.domain.models.CoffeeImageModel
import com.luczka.mycoffee.ui.models.CoffeeImageUiState

fun CoffeeImageModel.toUiState() : CoffeeImageUiState {
    return CoffeeImageUiState(
        coffeeImageId = coffeeImageId,
        uri = uri,
        filename = filename,
        index = index,
    )
}

fun List<CoffeeImageModel>.toUiState() : List<CoffeeImageUiState> {
    return this.map { it.toUiState() }
}

fun CoffeeImageUiState.toModel() : CoffeeImageModel {
    return CoffeeImageModel(
        coffeeImageId = coffeeImageId,
        uri = uri,
        filename = filename ?: "",
        index = index
    )
}

fun List<CoffeeImageUiState>.toModel() : List<CoffeeImageModel> {
    return this.map { it.toModel() }
}