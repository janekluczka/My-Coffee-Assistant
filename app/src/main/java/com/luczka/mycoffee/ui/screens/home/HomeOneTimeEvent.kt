package com.luczka.mycoffee.ui.screens.home

sealed class HomeOneTimeEvent {
    data object NavigateToBrewAssistant : HomeOneTimeEvent()
    data class NavigateToBrewDetails(val brewId: Long) : HomeOneTimeEvent()
    data class NavigateToCoffeeDetails(val coffeeId: Long) : HomeOneTimeEvent()
}