package com.luczka.mycoffee.ui.screens.coffees

sealed class CoffeesAction {
    data object NavigateUp : CoffeesAction()
    data object NavigateToAddCoffee : CoffeesAction()
    data class OnSelectedFilterChanged(val filter: CoffeeFilterUiState) : CoffeesAction()
    data class NavigateToCoffeeDetails(val coffeeId: Long) : CoffeesAction()
    data class OnItemActionsExpanded(val coffeeId: Long) : CoffeesAction()
    data class OnItemActionsCollapsed(val coffeeId: Long) : CoffeesAction()
    data class OnFavouriteItemClicked(val coffeeId: Long): CoffeesAction()
    data class OnEditClicked(val coffeeId: Long): CoffeesAction()
    data class OnDeleteItemClicked(val coffeeId: Long): CoffeesAction()
}