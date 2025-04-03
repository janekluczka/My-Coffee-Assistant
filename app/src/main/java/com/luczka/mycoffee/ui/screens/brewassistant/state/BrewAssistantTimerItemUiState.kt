package com.luczka.mycoffee.ui.screens.brewassistant.state

import android.annotation.SuppressLint
import com.luczka.mycoffee.ui.components.custom.doubleverticalpager.DoubleVerticalPagerState

data class BrewAssistantTimerItemUiState(
    val openPicker: Boolean,
    val openDialog: Boolean,
    val timeDoubleVerticalPagerState: DoubleVerticalPagerState,
    val isTimerRunning: Boolean,
    val timeInSeconds: Int,
) {
    @SuppressLint("DefaultLocale")
    fun formattedTime(): String {
        val minutes = timeInSeconds / 60
        val seconds = timeInSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun maxTimeInSeconds(): Int {
        return timeDoubleVerticalPagerState.leftPagerItems.last() * 60 + timeDoubleVerticalPagerState.rightPagerItems.last()
    }

    fun isMaxTimeReached(): Boolean {
        return timeInSeconds == maxTimeInSeconds()
    }
}