package com.luczka.mycoffee.ui.screens.brewassistant

sealed class BrewAssistantOneTimeEvent {
    data object NavigateUp : BrewAssistantOneTimeEvent()
    data class NavigateToBrewRating(val brewId: Long) : BrewAssistantOneTimeEvent()
}