package com.luczka.mycoffee.util

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

fun String.isPositiveInt(): Boolean {
    val value = this.toIntOrNull() ?: return false
    return value > 0
}

fun String.isNotPositiveInt(): Boolean {
    return !this.isPositiveInt()
}