package com.luczka.mycoffee.ui.screens.coffeedetails

sealed class CoffeeDetailsOneTimeEvent {
    data object NavigateUp : CoffeeDetailsOneTimeEvent()
    data class NavigateToCoffeeInput(val coffeeId: Long) : CoffeeDetailsOneTimeEvent()
}