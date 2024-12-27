package com.luczka.mycoffee.ui.navigation

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class DefaultMainNavigator(override val startMainNavHostRoute: MainNavHostRoute) : MainNavigator {

    private val _mainNavigationActions = Channel<MainNavigationAction>()
    override val mainNavigationActions = _mainNavigationActions.receiveAsFlow()

    override suspend fun navigate(
        mainNavHostRoute: MainNavHostRoute,
        navOptions: NavOptionsBuilder.() -> Unit
    ) {
        _mainNavigationActions.send(
            MainNavigationAction.Navigate(
                route = mainNavHostRoute,
                navOptions = navOptions
            )
        )
    }

    override suspend fun navigateUp() {
        _mainNavigationActions.send(MainNavigationAction.NavigateUp)
    }
}