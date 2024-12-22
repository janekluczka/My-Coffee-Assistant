package com.luczka.mycoffee.ui.screens.brewdetails

sealed class BrewDetailsAction {
    data object NavigateUp : BrewDetailsAction()
    data object OnDeleteClicked : BrewDetailsAction()
}