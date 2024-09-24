package com.luczka.mycoffee.ui.screens.history

sealed class HistoryAction {
    data class NavigateToHistoryDetails(val brewId: Int) : HistoryAction()
}