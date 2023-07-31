package com.coffee.mycoffeeassistant.util

import android.graphics.Bitmap

fun Bitmap.createSquareBitmap(): Bitmap {
    return if (this.width >= this.height) {
        val xOffset = this.width / 2 - this.height / 2
        Bitmap.createBitmap(this, xOffset, 0, this.height, this.height)
    } else {
        val yOffset = this.height / 2 - this.width / 2
        Bitmap.createBitmap(this, 0, yOffset, this.width, this.width)
    }
}