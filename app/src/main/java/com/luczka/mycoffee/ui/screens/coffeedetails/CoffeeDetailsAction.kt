package com.luczka.mycoffee.ui.screens.coffeedetails

sealed class CoffeeDetailsAction {
    data object NavigateUp : CoffeeDetailsAction()
    data object OnFavouriteClicked: CoffeeDetailsAction()
    data class OnEditClicked(val coffeeId: Long): CoffeeDetailsAction()
    data object ShowDeleteDialog : CoffeeDetailsAction()
    data object OnDeleteClicked: CoffeeDetailsAction()
}