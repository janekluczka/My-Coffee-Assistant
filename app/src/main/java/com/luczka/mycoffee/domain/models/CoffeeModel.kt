package com.luczka.mycoffee.domain.models

data class CoffeeModel(
    val id: Int,
    val name: String,
    val brand: String,
    val amount: Float?,
    val scaScore: Float?,
    val roast: RoastModel?,
    val process: ProcessModel?,
    val isFavourite: Boolean,
    val imageFile240x240: String?,
    val imageFile360x360: String?,
    val imageFile480x480: String?,
    val imageFile720x720: String?,
    val imageFile960x960: String?
)
