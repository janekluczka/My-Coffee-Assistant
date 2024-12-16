package com.luczka.mycoffee.ui.models

import android.net.Uri

data class CoffeeImageUiState(
    val coffeeImageId: Long = 0L,
    val uri: Uri,
    val filename: String? = null,
    val index: Int
)