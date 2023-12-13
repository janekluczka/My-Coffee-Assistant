package com.luczka.mycoffee.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import com.luczka.mycoffee.R

enum class MyCoffeeNavigationType {
    BOTTOM_NAVIGATION,
    NAVIGATION_RAIL
}

enum class HomeTabs(
    val route: String,
    @StringRes val stringResource: Int,
    @DrawableRes val painterResource: Int
) {
    HOME(
        stringResource = R.string.tab_home,
        painterResource = R.drawable.ic_baseline_home,
        route = MyCoffeeDestinations.ROUTE_HOME
    ),
    HISTORY(
        stringResource = R.string.tab_history,
        painterResource = R.drawable.ic_baseline_auto_awesome_motion,
        route = MyCoffeeDestinations.ROUTE_HISTORY
    ),
    MY_BAGS(
        stringResource = R.string.tab_my_bags,
        painterResource = R.drawable.ic_baseline_auto_awesome_motion,
        route = MyCoffeeDestinations.ROUTE_MY_BAGS
    ),
    RECIPES(
        stringResource = R.string.tab_recipes,
        painterResource = R.drawable.ic_baseline_format_list_bulleted,
        route = MyCoffeeDestinations.ROUTE_RECIPES
    ),
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainRoute(
    widthSizeClass: WindowWidthSizeClass,
    navController: NavHostController,
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
                bottomBar = {
                    MyCoffeeNavigationBar(
                        navController = navController
                    )
                }
            ) { innerPadding ->
                Box(modifier = Modifier.fillMaxSize()) {
                    MyCoffeeNavHost(
                        widthSizeClass = widthSizeClass,
                        navController = navController,
                        innerPadding = innerPadding,
                        navigateToAssistant = navigateToAssistant,
                        navigateToAddCoffee = navigateToAddCoffee,
                        navigateToEditCoffee = navigateToEditCoffee
                    )
                }
            }
        }

        MyCoffeeNavigationType.NAVIGATION_RAIL -> {
            Row(modifier = Modifier.fillMaxSize()) {
                MyCoffeeNavigationRail(navController = navController)
                MyCoffeeNavHost(
                    widthSizeClass = widthSizeClass,
                    navController = navController,
                    innerPadding = PaddingValues(),
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
    val currentDestination = navBackStackEntry?.destination
    val homeTabs = HomeTabs.values()

    NavigationRail(modifier = Modifier.fillMaxHeight()) {
        Spacer(modifier = Modifier.weight(1f))
        homeTabs.forEach { tab ->
            NavigationRailItem(
                icon = {
                    Icon(
                        painter = painterResource(id = tab.painterResource),
                        contentDescription = null
                    )
                },
                label = { Text(text = stringResource(id = tab.stringResource)) },
                selected = currentDestination?.hierarchy
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
    val currentDestination = navBackStackEntry?.destination
    val homeTabs = HomeTabs.values()

    NavigationBar(tonalElevation = 0.dp) {
        homeTabs.forEach { tab ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(tab.painterResource),
                        contentDescription = null
                    )
                },
                label = { Text(text = stringResource(id = tab.stringResource)) },
                selected = currentDestination?.hierarchy
                    ?.any { it.route?.contains(tab.route) == true } == true,
                onClick = {
                    navController.navigate(tab.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}