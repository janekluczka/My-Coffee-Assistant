package com.luczka.mycoffee.ui.screens.coffeedetails

sealed class CoffeeDetailsAction {
    object NavigateUp : CoffeeDetailsAction()
    object OnFavouriteClicked: CoffeeDetailsAction()
    data class OnEditClicked(val coffeeId: Int): CoffeeDetailsAction()
    object OnDeleteClicked: CoffeeDetailsAction()
}