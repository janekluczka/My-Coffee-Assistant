package com.luczka.mycoffee.util

fun Int.toDigitList(): List<Int> {
    return this.toString().map { it.toString().toInt() }
}