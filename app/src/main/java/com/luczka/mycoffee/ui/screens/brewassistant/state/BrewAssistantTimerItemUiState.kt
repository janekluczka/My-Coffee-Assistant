package com.luczka.mycoffee.ui.screens.brewassistant.state

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import com.luczka.mycoffee.R

@SuppressLint("DefaultLocale")
data class BrewAssistantTimerItemUiState(
    val openPicker: Boolean = false,
    val openDialog: Boolean = false,
    val minutesPageIndex: Int = 0,
    val minutesPagerItems: List<Int> = (0..59).toList(),
    val minutesPagerItemsTextFormatter: ((Int) -> String)? = { String.format("%02d", it) },
    val secondsPageIndex: Int = 0,
    val secondsPagerItems: List<Int> = (0..59).toList(),
    val secondsPagerItemsTextFormatter: ((Int) -> String)? = { String.format("%02d", it) },
    @StringRes val separatorRes: Int = R.string.separator_time,
    val isRunning: Boolean = false,
    val timeInSeconds: Int = 0,
) {
    fun formattedTime(): String {
        val minutes = timeInSeconds / 60
        val seconds = timeInSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun selectedMinutes(): Int {
        return minutesPagerItems[minutesPageIndex]
    }

    fun selectedSeconds(): Int {
        return secondsPagerItems[secondsPageIndex]
    }

    fun minutesAt(index: Int): Int {
        return minutesPagerItems[index]
    }

    fun secondsAt(index: Int): Int {
        return secondsPagerItems[index]
    }

    fun maxTimeInSeconds(): Int {
        return minutesPagerItems.last() * 60 + secondsPagerItems.last()
    }

    fun isMaxTimeReached(): Boolean {
        return timeInSeconds == maxTimeInSeconds()
    }
}