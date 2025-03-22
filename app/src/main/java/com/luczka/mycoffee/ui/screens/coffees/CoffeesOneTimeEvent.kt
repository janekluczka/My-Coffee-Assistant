package com.luczka.mycoffee.ui.screens.coffees

sealed class CoffeesOneTimeEvent {
    data object NavigateUp : CoffeesOneTimeEvent()
    data object NavigateToAddCoffee : CoffeesOneTimeEvent()
    data class NavigateToCoffeeDetails(val coffeeId: Long) : CoffeesOneTimeEvent()
    data class NavigateToEditCoffee(val coffeeId: Long) : CoffeesOneTimeEvent()
}