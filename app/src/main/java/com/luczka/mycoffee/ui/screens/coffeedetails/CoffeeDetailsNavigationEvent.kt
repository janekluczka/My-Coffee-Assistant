package com.luczka.mycoffee.ui.screens.coffeedetails

sealed class CoffeeDetailsNavigationEvent {
    data object NavigateUp : CoffeeDetailsNavigationEvent()
    data class NavigateToCoffeeInput(val coffeeId: Long) : CoffeeDetailsNavigationEvent()
}