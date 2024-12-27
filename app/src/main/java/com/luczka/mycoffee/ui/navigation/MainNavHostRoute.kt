package com.luczka.mycoffee.ui.navigation

import kotlinx.serialization.Serializable

sealed interface MainNavHostRoute {

    @Serializable
    data object Main : MainNavHostRoute

    @Serializable
    data class BrewDetails(val brewId: Long) : MainNavHostRoute

    @Serializable
    data object BrewAssistant : MainNavHostRoute

    @Serializable
    data class BrewRating(val brewId: Long) : MainNavHostRoute

    @Serializable
    data class CoffeeDetails(val coffeeId: Long) : MainNavHostRoute

    @Serializable
    data class CoffeeInput(val coffeeId: Long? = null) : MainNavHostRoute

    @Serializable
    data class EquipmentInput(val equipmentId: Long? = null) : MainNavHostRoute

}