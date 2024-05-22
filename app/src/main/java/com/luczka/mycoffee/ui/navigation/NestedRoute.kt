package com.luczka.mycoffee.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun NestedRoute(
    widthSizeClass: WindowWidthSizeClass,
    nestedNavController: NavHostController,
    navigateToAssistant: () -> Unit,
    navigateToAddCoffee: () -> Unit,
    navigateToEditCoffee: (Int) -> Unit
) {
    val navigationType = when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> MyCoffeeNavigationType.BOTTOM_NAVIGATION
        WindowWidthSizeClass.Medium -> MyCoffeeNavigationType.NAVIGATION_RAIL
        WindowWidthSizeClass.Expanded -> MyCoffeeNavigationType.NAVIGATION_RAIL
        else -> MyCoffeeNavigationType.BOTTOM_NAVIGATION
    }

    when (navigationType) {
        MyCoffeeNavigationType.BOTTOM_NAVIGATION -> {
            Scaffold(
                bottomBar = { MyCoffeeNavigationBar(navController = nestedNavController) }
            ) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding)) {
                    MyCoffeeNestedNavHost(
                        modifier = Modifier.weight(1f),
                        widthSizeClass = widthSizeClass,
                        navController = nestedNavController,
                        navigateToAssistant = navigateToAssistant,
                        navigateToAddCoffee = navigateToAddCoffee,
                        navigateToEditCoffee = navigateToEditCoffee
                    )
                    Divider()
                }
            }
        }

        MyCoffeeNavigationType.NAVIGATION_RAIL -> {
            Row(modifier = Modifier.fillMaxSize()) {
                MyCoffeeNavigationRail(navController = nestedNavController)
                MyCoffeeNestedNavHost(
                    modifier = Modifier,
                    widthSizeClass = widthSizeClass,
                    navController = nestedNavController,
                    navigateToAssistant = navigateToAssistant,
                    navigateToAddCoffee = navigateToAddCoffee,
                    navigateToEditCoffee = navigateToEditCoffee
                )
            }
        }
    }
}

@Composable
private fun MyCoffeeNavigationRail(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    NavigationRail(modifier = Modifier.fillMaxHeight()) {
        Spacer(modifier = Modifier.weight(1f))
        HomeTabs.values().forEach { tab ->
            NavigationRailItem(
                icon = {
                    Icon(
                        painter = painterResource(id = tab.painterResource),
                        contentDescription = null
                    )
                },
                label = { Text(text = stringResource(id = tab.stringResource)) },
                selected = navBackStackEntry?.destination?.hierarchy
                    ?.any { it.route?.contains(tab.route) == true } == true,
                onClick = {
                    navController.navigate(tab.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun MyCoffeeNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    NavigationBar(tonalElevation = 0.dp) {
        HomeTabs.values().forEach { tab ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(tab.painterResource),
                        contentDescription = null
                    )
                },
                label = { Text(text = stringResource(id = tab.stringResource)) },
                selected = navBackStackEntry?.destination?.hierarchy
                    ?.any { it.route?.contains(tab.route) == true } == true,
                onClick = {
                    navController.navigate(tab.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}