package com.luczka.mycoffee.ui.navigation

import kotlinx.serialization.Serializable

data object MainNavHostRoutes {

    @Serializable
    data object Main

    @Serializable
    data class BrewDetails(val brewId: Long)

    @Serializable
    data object Assistant

    @Serializable
    data class AssistantRating(val brewId: Long)

    @Serializable
    data class CoffeeDetails(val coffeeId: Long)

    @Serializable
    data class CoffeeInput(val coffeeId: Long? = null)

    @Serializable
    data class EquipmentInput(val equipmentId: Long? = null)

}