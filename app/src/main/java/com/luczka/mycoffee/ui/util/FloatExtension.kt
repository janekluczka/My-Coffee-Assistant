package com.luczka.mycoffee.ui.util

import java.util.Locale

fun Float.toStringWithOneDecimalPoint(): String {
    return String.format(Locale.US, "%.1f", this)
}

fun Float.toStringWithTwoDecimalPoints(): String {
    return String.format(Locale.US, "%.2f", this)
}