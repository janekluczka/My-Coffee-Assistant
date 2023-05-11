@file:OptIn(ExperimentalMaterial3Api::class)

package com.coffee.mycoffeeassistant.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.coffee.mycoffeeassistant.R
import com.coffee.mycoffeeassistant.ui.components.AddCoffeeFloatingActionButton
import com.coffee.mycoffeeassistant.ui.components.BrewAssistantFloatingActionButton
import com.coffee.mycoffeeassistant.ui.navigation.Screen

@Composable
fun MyCoffeeAssistantApp() {
    val appState = rememberMyCoffeeAssistantAppState()
    val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.app_name_short),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    if (!appState.navigationBarDestinations.any { it.route == currentDestination?.route })
                        if (appState.navController.previousBackStackEntry != null) {
                            IconButton(onClick = { appState.navController.navigateUp() }) {
                                Icon(
                                    painterResource(id = R.drawable.ic_baseline_arrow_back),
                                    contentDescription = "Back"
                                )
                            }
                        }
                }
            )
        },
        floatingActionButton = {
            when (currentDestination?.route) {
                Screen.Home.route -> BrewAssistantFloatingActionButton {
                    appState.navController.navigate(Screen.BrewAssistant.route)
                }

                Screen.Cupboard.route -> AddCoffeeFloatingActionButton {
                    appState.navController.navigate(Screen.AddCoffee.route)
                }
            }
        },
        floatingActionButtonPosition = when (currentDestination?.route) {
            Screen.Cupboard.route -> Screen.Cupboard.fabPosition
            else -> FabPosition.Center
        },
        bottomBar = {
            NavigationBar {
                appState.navigationBarDestinations.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painterResource(screen.painterResource),
                                contentDescription = null
                            )
                        },
                        label = { Text(stringResource(screen.stringResource)) },
                        selected = currentDestination?.hierarchy?.any {
//                                it.route == screen.route
                            it.route?.contains(screen.route) ?: false
                        } == true,
                        onClick = {
                            appState.navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(appState.navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            MyCoffeeAssistantNavHost(appState, innerPadding)
        }
    }
}