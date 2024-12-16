package com.luczka.mycoffee.ui.screens.historydetails

sealed class HistoryDetailsAction {
    data object NavigateUp : HistoryDetailsAction()
    data object OnDeleteClicked : HistoryDetailsAction()
}