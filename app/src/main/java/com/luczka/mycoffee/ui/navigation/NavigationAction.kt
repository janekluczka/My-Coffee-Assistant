package com.luczka.mycoffee.ui.navigation

import androidx.navigation.NavOptionsBuilder

sealed interface MainNavigationAction {

    data class Navigate(
        val route: MainNavHostRoute,
        val navOptions: NavOptionsBuilder.() -> Unit = {}
    ) : MainNavigationAction

    data object NavigateUp : MainNavigationAction

}