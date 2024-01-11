package com.luczka.mycoffee.util

import java.util.Locale

fun Float.toStringWithOneDecimalPoint(): String {
    return String.format(Locale.US, "%.1f", this)
}

fun Float.toStringWithTwoDecimalPoints(): String {
    return String.format(Locale.US, "%.2f", this)
}

fun Float.getIntegerPart(): Int {
    return this.toInt()
}

fun Float.getDecimalPart(): Int {
    return ((this - this.getIntegerPart()) * 10).toInt()
}