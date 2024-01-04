package com.luczka.mycoffee.util

fun <K, V> Map<K, V>.hasMultipleElements(): Boolean {
    return size > 1
}