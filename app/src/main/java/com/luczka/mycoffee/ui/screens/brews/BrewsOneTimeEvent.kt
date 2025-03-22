package com.luczka.mycoffee.ui.screens.brews

sealed class BrewsOneTimeEvent {
    data object NavigateToAssistant : BrewsOneTimeEvent()
    data class NavigateToBrewDetails(val brewId: Long) : BrewsOneTimeEvent()
}