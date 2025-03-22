package com.luczka.mycoffee.ui.screens.brewdetails

sealed class BrewDetailsOneTimeEvent {
    data object NavigateUp : BrewDetailsOneTimeEvent()
}