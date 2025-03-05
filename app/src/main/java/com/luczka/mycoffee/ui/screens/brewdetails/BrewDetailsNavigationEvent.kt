package com.luczka.mycoffee.ui.screens.brewdetails

sealed class BrewDetailsNavigationEvent {
    data object NavigateUp : BrewDetailsNavigationEvent()
}