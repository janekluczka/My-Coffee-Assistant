package com.luczka.mycoffee.ui.screens.coffees

sealed class CoffeesNavigationEvent {
    data object NavigateUp : CoffeesNavigationEvent()
    data object NavigateToAddCoffee : CoffeesNavigationEvent()
    data class NavigateToCoffeeDetails(val coffeeId: Long) : CoffeesNavigationEvent()
    data class NavigateToEditCoffee(val coffeeId: Long) : CoffeesNavigationEvent()
}