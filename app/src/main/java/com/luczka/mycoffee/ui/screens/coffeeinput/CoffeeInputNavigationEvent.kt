package com.luczka.mycoffee.ui.screens.coffeeinput

sealed class CoffeeInputNavigationEvent {
    data object NavigateUp : CoffeeInputNavigationEvent()
}