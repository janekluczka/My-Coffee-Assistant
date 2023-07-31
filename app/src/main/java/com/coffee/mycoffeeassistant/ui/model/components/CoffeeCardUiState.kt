package com.coffee.mycoffeeassistant.ui.model.components

data class CoffeeCardUiState(
    val id: Int,
    val name: String,
    val brand: String,
    val hasImage: Boolean = false,
    var imageFile240x240: String = "",
    var imageFile360x360: String = "",
    var imageFile480x480: String = "",
    var imageFile720x720: String = "",
    var imageFile960x960: String = "",
)
