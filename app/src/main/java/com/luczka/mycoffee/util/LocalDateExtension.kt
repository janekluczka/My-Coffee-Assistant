package com.luczka.mycoffee.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.BASIC_ISO_DATE

fun LocalDate.formatToBasicIsoDate(): String? {
    return this.format(dateTimeFormatter)
}

object LocalDateParser {
    fun parseBasicIsoDate(text: CharSequence): LocalDate {
        return LocalDate.parse(text, dateTimeFormatter)
    }
}

