package com.luczka.mycoffee.ui.screens.coffeeinput

sealed class CoffeeInputOneTimeEvent {
    data object NavigateUp : CoffeeInputOneTimeEvent()
}