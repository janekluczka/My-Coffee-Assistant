package com.luczka.mycoffee.ui.screens.coffees

import com.luczka.mycoffee.ui.models.CoffeeUiState

sealed class CoffeesAction {
    data object OnBackClicked : CoffeesAction()
    data object OnAddCoffeeClicked : CoffeesAction()
    data class OnFilterClicked(val filter: CoffeeFilterUiState) : CoffeesAction()
    data class OnCoffeeClicked(val coffeeId: Long) : CoffeesAction()
    data class OnItemActionsExpanded(val coffeeId: Long) : CoffeesAction()
    data class OnItemActionsCollapsed(val coffeeId: Long) : CoffeesAction()
    data class OnItemIsFavouriteClicked(val coffeeUiState: CoffeeUiState): CoffeesAction()
    data class OnEditClicked(val coffeeId: Long): CoffeesAction()
    data class OnItemDeleteClicked(val coffeeUiState: CoffeeUiState): CoffeesAction()
}