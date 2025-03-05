package com.luczka.mycoffee.ui.screens.brewassistant

sealed class BrewAssistantNavigationEvent {
    data object NavigateUp : BrewAssistantNavigationEvent()
    data class NavigateToBrewRating(val brewId: Long) : BrewAssistantNavigationEvent()
}