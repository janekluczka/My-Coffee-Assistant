package com.luczka.mycoffee.ui.screens.coffeedetails

sealed class CoffeeDetailsAction {
    data object NavigateUp : CoffeeDetailsAction()
    data object OnFavouriteClicked: CoffeeDetailsAction()
    data object OnEditClicked : CoffeeDetailsAction()
    data object ShowDeleteDialog : CoffeeDetailsAction()
    data object HideDeleteDialog : CoffeeDetailsAction()
    data object OnDeleteClicked: CoffeeDetailsAction()
}