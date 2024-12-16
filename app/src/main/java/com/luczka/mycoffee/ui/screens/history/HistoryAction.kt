package com.luczka.mycoffee.ui.screens.history

sealed class HistoryAction {
    data object NavigateToAssistant: HistoryAction()
    data class NavigateToBrewDetails(val brewId: Long) : HistoryAction()
}