package com.luczka.mycoffee.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun MainRoute(
    widthSizeClass: WindowWidthSizeClass,
    nestedNavController: NavHostController,
    onMenuButtonClick: () -> Unit,
    navigateToAssistant: () -> Unit,
    navigateToBrewDetails: (brewId: Long) -> Unit,
    navigateToCoffeeDetails: (coffeeId: Long) -> Unit,
    navigateToCoffeeInput: (coffeeId: Long?) -> Unit,
    navigateToEquipmentInput: (equipmentId: Long?) -> Unit
) {
    val navigationType = when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> NavigationType.BOTTOM_NAVIGATION
        WindowWidthSizeClass.Medium -> NavigationType.NAVIGATION_RAIL
        WindowWidthSizeClass.Expanded -> NavigationType.NAVIGATION_RAIL
        else -> NavigationType.BOTTOM_NAVIGATION
    }

    Scaffold(
        bottomBar = {
            if (navigationType == NavigationType.BOTTOM_NAVIGATION) {
                Column {
                    Divider()
                    MyCoffeeNavigationBar(navController = nestedNavController)
                }
            }
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (navigationType == NavigationType.NAVIGATION_RAIL) {
                MyCoffeeNavigationRail(navController = nestedNavController)
            }
            MyCoffeeNestedNavHost(
                modifier = Modifier,
                widthSizeClass = widthSizeClass,
                navController = nestedNavController,
                navigateToAssistant = navigateToAssistant,
                navigateToBrewDetails = navigateToBrewDetails,
                navigateToCoffeeDetails = navigateToCoffeeDetails,
                navigateToCoffeeInput = navigateToCoffeeInput,
                navigateToEquipmentInput = navigateToEquipmentInput
            )
        }
    }
}

@Composable
private fun MyCoffeeNavigationRail(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationRail(modifier = Modifier.fillMaxHeight()) {
        Spacer(modifier = Modifier.weight(1f))
        topLevelRoutes.forEach { topLevelRoute ->
            NavigationRailItem(
                icon = {
                    Icon(
                        painter = painterResource(id = topLevelRoute.drawableRes),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = topLevelRoute.stringRes),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(topLevelRoute.route::class)
                } == true,
                onClick = {
                    navController.navigate(topLevelRoute.route) {
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
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        tonalElevation = 0.dp,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        topLevelRoutes.forEach { topLevelRoute ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(topLevelRoute.drawableRes),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = topLevelRoute.stringRes),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(topLevelRoute.route::class)
                } == true,
                onClick = {
                    navController.navigate(topLevelRoute.route) {
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