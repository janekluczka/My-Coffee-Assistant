package com.luczka.mycoffee.ui.screens.home

sealed class HomeNavigationEvent {
    object NavigateToBrewAssistant : HomeNavigationEvent()
    data class NavigateToBrewDetails(val brewId: Long) : HomeNavigationEvent()
    data class NavigateToCoffeeDetails(val coffeeId: Long) : HomeNavigationEvent()
}