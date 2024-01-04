package com.luczka.mycoffee.util

import java.util.Locale

fun Float.toStringWithOneDecimalPoint(): String {
    return String.format(Locale.US, "%.1f", this)
}

fun Float.toStringWithTwoDecimalPoints(): String {
    return String.format(Locale.US, "%.2f", this)
}

fun Float.getWholeNumber(): Int {
    return this.toInt()
}

fun Float.getFractionalPart(): Int {
    return ((this - this.getWholeNumber()) * 10).toInt()
}