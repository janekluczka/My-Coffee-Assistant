package com.luczka.mycoffee.domain.models

import android.net.Uri

data class CoffeeImageModel(
    val coffeeImageId: Long,
    val uri: Uri,
    val filename: String,
    val index: Int,
    val coffeeId: Int = 0,
)