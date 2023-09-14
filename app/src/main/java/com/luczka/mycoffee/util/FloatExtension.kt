package com.luczka.mycoffee.util

fun Float.toStringWithOneDecimalPoint(): String {
    return String.format("%.1f", this)
}