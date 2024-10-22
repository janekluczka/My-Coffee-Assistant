package com.luczka.mycoffee.ui.screens.coffees

import com.luczka.mycoffee.ui.models.CoffeeFilterUiState

sealed class CoffeesAction {
    object NavigateUp : CoffeesAction()
    object NavigateToAddCoffee : CoffeesAction()
    data class OnSelectedFilterChanged(val coffeeFilterUiState: CoffeeFilterUiState) : CoffeesAction()
    data class NavigateToCoffeeDetails(val coffeeId: Int) : CoffeesAction()
}