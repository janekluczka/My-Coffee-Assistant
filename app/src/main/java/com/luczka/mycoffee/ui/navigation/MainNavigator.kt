package com.luczka.mycoffee.ui.navigation

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.flow.Flow

interface MainNavigator {
    val startMainNavHostRoute: MainNavHostRoute
    val mainNavigationActions: Flow<MainNavigationAction>

    suspend fun navigate(
        mainNavHostRoute: MainNavHostRoute,
        navOptions: NavOptionsBuilder.() -> Unit = {}
    )

    suspend fun navigateUp()
}