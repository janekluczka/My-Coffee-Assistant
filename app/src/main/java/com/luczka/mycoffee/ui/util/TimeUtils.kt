package com.luczka.mycoffee.ui.util

object TimeFormatter {

    fun formatTime(seconds: Int?): String {
        if (seconds == null) return "--:--"
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

}
