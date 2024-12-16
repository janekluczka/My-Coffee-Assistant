package com.luczka.mycoffee.ui.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object DateFormattingUtil {

    private val shortDateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)

    fun formatShortDate(date: LocalDate): String {
        return date.format(shortDateFormatter)
    }
}