package com.luczka.mycoffee.domain.models

import java.time.LocalDate

data class BrewModel(
    val id: Long,
    val date: LocalDate,
    val coffeeAmount: Float,
    val coffeeRatio: Int,
    val waterRatio: Int,
    val waterAmount: Float,
    val rating: Int?,
    val notes: String,
    val brewedCoffees: List<BrewedCoffeeModel>
)
