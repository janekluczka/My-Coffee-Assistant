package com.luczka.mycoffee.ui.screens.brews

sealed class BrewsNavigationEvent {
    data object NavigateToAssistant : BrewsNavigationEvent()
    data class NavigateToBrewDetails(val brewId: Long) : BrewsNavigationEvent()
}