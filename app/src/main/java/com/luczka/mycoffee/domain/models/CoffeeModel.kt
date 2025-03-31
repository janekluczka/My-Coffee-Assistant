package com.luczka.mycoffee.domain.models

import java.time.LocalDate

data class CoffeeModel(
    val coffeeId: Long,
    val coffeeImages: List<CoffeeImageModel>,
    val brand: String,
    val name: String,
    val roast: RoastModel?,
    val process: ProcessModel?,
    val plantation: String?,
    val altitude: Int?,
    val scaScore: Float?,
    val additionalInformation: String?,
    val isFavourite: Boolean,
    val updatedOn: LocalDate,
    val addedOn: LocalDate
)