package com.coffee.mycoffeeassistant.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.coffee.mycoffeeassistant.ui.navigation.cupboardNavGraph
import com.coffee.mycoffeeassistant.ui.navigation.homeNavGraph
import com.coffee.mycoffeeassistant.ui.navigation.recipesNavGraph

@Composable
fun MyCoffeeAssistantNavHost(
    appState: MyCoffeeAssistantAppState,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = appState.navController,
        startDestination = "home",
        route = "root",
        modifier = Modifier.padding(innerPadding)
    ) {
        homeNavGraph(appState)
        cupboardNavGraph(appState)
        recipesNavGraph(appState)
    }
}

