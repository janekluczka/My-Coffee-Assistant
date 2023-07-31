package com.coffee.mycoffeeassistant.util

fun String.isPositiveFloat(): Boolean {
    val value = this.toFloatOrNull() ?: return false
    return value > 0
}

fun String.isNotPositiveFloat(): Boolean {
    return !this.isPositiveFloat()
}

fun String.isNegativeFloat(): Boolean {
    val value = this.toFloatOrNull() ?: return false
    return value < 0
}

fun String.isNotNegativeFloat(): Boolean {
    return !this.isNegativeFloat()
}