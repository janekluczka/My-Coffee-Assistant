package com.luczka.mycoffee.domain.models

data class CoffeesDataModel(
    val hasAny: Boolean,
    val filteredCoffees: List<CoffeeModel>
)