package com.coffee.mycoffeeassistant.ui.model.screens

import com.coffee.mycoffeeassistant.ui.model.components.CoffeeCardUiState
import java.time.LocalTime
import java.time.ZoneId

data class HomeUiState(
    val userGreeting: String = getUserGreeting(),
    val lowAmountCoffeeUiStateList: List<CoffeeCardUiState> = emptyList(),
    val oldCoffeeUiStateList:  List<CoffeeCardUiState> = emptyList(),
)

fun getUserGreeting(): String {
    val currentTime = LocalTime.now(ZoneId.systemDefault())
    return when {
        currentTime.isBefore(LocalTime.of(4, 0)) -> "It's late. Get some sleep"
        currentTime.isBefore(LocalTime.of(12, 0)) -> "Good morning"
        currentTime.isBefore(LocalTime.of(17, 0)) -> "Good afternoon"
        else -> "Good evening"
    }
}