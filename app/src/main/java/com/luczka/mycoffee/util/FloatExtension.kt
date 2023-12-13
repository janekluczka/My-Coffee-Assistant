package com.luczka.mycoffee.util

import java.util.Locale

fun Float.toStringWithOneDecimalPoint(): String {
    return String.format(Locale.US, "%.1f", this)
}