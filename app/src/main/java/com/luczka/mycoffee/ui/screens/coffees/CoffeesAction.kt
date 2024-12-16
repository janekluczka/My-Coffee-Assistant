package com.luczka.mycoffee.ui.screens.coffees

import com.luczka.mycoffee.ui.models.CoffeeFilterUiState

sealed class CoffeesAction {
    data object NavigateUp : CoffeesAction()
    data object NavigateToAddCoffee : CoffeesAction()
    data class OnSelectedFilterChanged(val coffeeFilterUiState: CoffeeFilterUiState) : CoffeesAction()
    data class NavigateToCoffeeDetails(val coffeeId: Long) : CoffeesAction()
    data class OnItemActionsExpanded(val coffeeId: Long) : CoffeesAction()
    data class OnCollapseItemActions(val coffeeId: Long) : CoffeesAction()
    data object OnFavouriteClicked: CoffeesAction()
    data class OnEditClicked(val coffeeId: Long): CoffeesAction()
    data object ShowDeleteDialog : CoffeesAction()
    data object OnDeleteClicked: CoffeesAction()
}