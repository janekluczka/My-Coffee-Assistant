package com.luczka.mycoffee.ui.screens.home

sealed class HomeAction {
    data object OnMenuClicked: HomeAction()
    data object NavigateToBrewAssistant : HomeAction()
    data class NavigateToBrewDetails(val brewId: Long) : HomeAction()
    data class NavigateToCoffeeDetails(val coffeeId: Long) : HomeAction()
}