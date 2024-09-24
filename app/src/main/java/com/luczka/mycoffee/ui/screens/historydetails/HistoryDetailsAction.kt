package com.luczka.mycoffee.ui.screens.historydetails

sealed class HistoryDetailsAction {
    object NavigateUp : HistoryDetailsAction()
    object OnDeleteClicked : HistoryDetailsAction()
}