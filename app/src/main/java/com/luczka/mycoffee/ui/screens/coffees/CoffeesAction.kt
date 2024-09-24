package com.luczka.mycoffee.ui.screens.coffees

import com.luczka.mycoffee.domain.models.CoffeeFilter

sealed class CoffeesAction {
    object NavigateUp : CoffeesAction()
    object NavigateToAddCoffee : CoffeesAction()
    data class OnSelectedFilterChanged(val coffeeFilter: CoffeeFilter) : CoffeesAction()
    data class NavigateToCoffeeDetails(val coffeeId: Int) : CoffeesAction()
}